@file:Suppress("MemberVisibilityCanBePrivate")

package com.hoanganhtuan95ptit.autobind

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.hoanganhtuan95ptit.autobind.utils.exts.createObject
import com.hoanganhtuan95ptit.autobind.utils.exts.reloadBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

object AutoBind {

    lateinit var application: Application


    private val map = ConcurrentHashMap<String, List<String>>()

    private val loadState = MutableStateFlow(false)


    fun init(application: Application) {

        if (::application.isInitialized) {
            return
        }

        this.application = application

        reload()
    }

    fun reload() = ProcessLifecycleOwner.get().lifecycleScope.launch(Dispatchers.IO) {

        loadState.tryEmit(false)

        val allBindings = this@AutoBind.application.reloadBinding()

        allBindings.groupBy({

            it.type
        }, {

            it.impl
        }).mapValues {

            it.value.toSet().toList()
        }.let {

            map.putAll(it)
        }

        loadState.tryEmit(true)
    }

    fun forceLoad() {

        reload()
    }


    fun <T> load(clazz: Class<T>): List<T> {

        return map[clazz.name]?.mapNotNull { it.createObject(clazz) }.orEmpty()
    }

    fun <T> loadAsync(clazz: Class<T>): Flow<List<T>> = loadState.mapNotNull {

        if (it) {
            load(clazz = clazz)
        } else {
            null
        }
    }

    fun <T> loadName(clazz: Class<T>): List<String> {

        return map[clazz.name].orEmpty()
    }

    fun <T> loadNameAsync(clazz: Class<T>): Flow<List<String>> = loadState.mapNotNull {

        if (it) {
            loadName(clazz = clazz)
        } else {
            null
        }
    }


    suspend fun awaitLoaded() = loadState.mapNotNull {

        if (it) {
            true
        } else {
            null
        }
    }.first()
}