package com.hoanganhtuan95ptit.autobind

import android.app.Application
import android.content.Context
import com.google.gson.Gson

internal data class Binding(
    val type: String,
    val impl: String
)

internal data class BindingsWrapper(
    val bindings: List<Binding>
)

object AutoBind {

    lateinit var application: Application


    private val allBindings = mutableListOf<Binding>()

    fun init(application: Application) {
        this.application = application
    }

    fun <T> load(clazz: Class<T>, userCache: Boolean = true): List<T> {

        val list = allBindings.filter {
            clazz.name == it.type
        }.mapNotNull {
            it.impl.createObject(clazz = clazz)
        }

        if (list.isNotEmpty() && userCache) {
            return list
        }

        allBindings.addAll(application.reloadBinding())

        return allBindings.filter {
            clazz.name == it.type
        }.mapNotNull {
            it.impl.createObject(clazz = clazz)
        }
    }


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
}