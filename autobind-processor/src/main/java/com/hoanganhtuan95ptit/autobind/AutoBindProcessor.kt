package com.hoanganhtuan95ptit.autobind

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.StandardLocation

@Suppress("unused")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class AutoBindProcessor : AbstractProcessor() {

    // Tên annotation mà processor sẽ xử lý
    private val annotationName = "com.hoanganhtuan95ptit.autobind.annotation.AutoBind"

    // Trả về tập annotation mà processor hỗ trợ
    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(annotationName)
    }

    // Trả về phiên bản source code mà processor hỗ trợ
    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    // Hàm chính xử lý annotation
    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {

        // Lấy tất cả class được annotate với @AutoService
        val elements = roundEnv.getElementsAnnotatedWith(
            processingEnv.elementUtils.getTypeElement(annotationName)
        )

        // Nếu không có class nào, dừng xử lý
        if (elements.isEmpty()) return false

        // Lấy moduleName từ Gradle kapt arguments (hoặc default "unknown-module")
        val moduleName = processingEnv.options["moduleName"] ?: "unknown-module"

        // JsonArray để chứa tất cả bindings
        val arr = JsonArray()

        for (element in elements) {

            // Lấy annotation AutoService trên class này
            val ann = element.annotationMirrors.first {
                it.annotationType.toString() == annotationName
            }

            // Lấy danh sách các interface/class trong value (AutoService có thể có nhiều interface)
            val forTypeList = ann.elementValues.entries
                .first { it.key.simpleName.toString() == "value" }
                .value.value as List<*>

            // Lấy tên class implement (fully-qualified)
            val implName = (element as TypeElement).qualifiedName.toString()

            // Duyệt qua từng interface/class trong value để tạo entry JSON
            for (forType in forTypeList) if (forType != null) {

                val forTypeName = forType.toString().removeSuffix(".class")

                val obj = JsonObject()
                obj.addProperty("type", forTypeName)
                obj.addProperty("impl", implName)
                arr.add(obj)
            }
        }


        // Nếu không có binding nào được tạo, dừng xử lý
        if (arr.isEmpty) return true

        // Tạo object JSON cuối cùng
        val json = JsonObject()
        json.add("bindings", arr)

        // Ghi file JSON vào CLASS_OUTPUT/assets/autobind/
        writeJsonFile(moduleName, json.toString())

        return true
    }

    // Hàm ghi file JSON
    private fun writeJsonFile(moduleName: String, content: String) = kotlin.runCatching {

        val fileObject = processingEnv.filer.createResource(
            StandardLocation.CLASS_OUTPUT,
            "", // package để trống
            "assets/autobind/$moduleName.json"
        )

        fileObject.openWriter().use {
            it.write(content)
        }
    }
}