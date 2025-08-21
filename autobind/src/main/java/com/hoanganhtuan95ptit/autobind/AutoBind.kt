package com.hoanganhtuan95ptit.autobind

import android.content.Context
import com.google.gson.Gson

internal data class Binding(
    val type: String,
    val impl: String
)

internal data class BindingsWrapper(
    val bindings: List<Binding>
)

internal fun <T> String.createObject(clazz: Class<T>) = runCatching {

    val instance = Class.forName(this).getDeclaredConstructor().newInstance()
    if (clazz.isInstance(instance)) clazz.cast(instance) else null
}.getOrElse {

    null
}

internal fun Context.reloadBinding() = runCatching {

    val gson = Gson()

    val allBindings = mutableListOf<Binding>()

    val fileNames = assets.list("autobind") ?: emptyArray()

    for (fileName in fileNames) if (fileName.endsWith(".json")) {

        // Mở file JSON từ assets và đọc toàn bộ nội dung
        val json = assets.open("autobind/$fileName")
            .bufferedReader()
            .use { it.readText() }

        // Parse JSON thành đối tượng BindingsWrapper
        val wrapper = gson.fromJson(json, BindingsWrapper::class.java)

        // Thêm tất cả bindings vào danh sách
        allBindings.addAll(wrapper.bindings)
    }

    allBindings
}.getOrElse {

    emptyList()
}

fun <T> Context.reloadService(clazz: Class<T>): List<T> {

    val allBindings = reloadBinding()

    return allBindings.filter {
        clazz.name == it.type
    }.mapNotNull {
        it.impl.createObject(clazz = clazz)
    }
}