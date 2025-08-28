package com.hoanganhtuan95ptit.autobind

import android.app.Application
import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList

object StartApp {

    private val handledList = CopyOnWriteArrayList<String>()

    fun init(application: Application) {

        AutoBind.init(application)

        ProcessLifecycleOwner.get().lifecycleScope.launch {

            AutoBind.loadAsync(Initializer::class.java).collect { list ->

                list.forEach {

                    if (it.javaClass.name in handledList) return@forEach

                    handledList.add(it.javaClass.name)
                    it.create(application)
                }
            }
        }
    }
}

interface Initializer {

    fun create(context: Context)
}