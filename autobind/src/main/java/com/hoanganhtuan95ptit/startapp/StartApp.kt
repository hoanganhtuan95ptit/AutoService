package com.hoanganhtuan95ptit.startapp

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.hoanganhtuan95ptit.autobind.AutoBind
import com.hoanganhtuan95ptit.autobind.utils.exts.createObject
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList

object StartApp {

    private val handledList = CopyOnWriteArrayList<String>()

    fun init(application: Application) {

        AutoBind.init(application)

        val clazz = ModuleInitializer::class.java

        ProcessLifecycleOwner.get().lifecycleScope.launch {

            AutoBind.loadNameAsync(clazz).collect { list ->

                list.forEach {

                    if (it in handledList) return@forEach
                    handledList.add(it)

                    it.createObject(clazz)?.create(application)
                }
            }
        }
    }
}