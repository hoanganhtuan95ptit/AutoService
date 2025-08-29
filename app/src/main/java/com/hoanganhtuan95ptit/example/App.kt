package com.hoanganhtuan95ptit.example

import android.app.Application
import android.content.Context
import com.google.android.play.core.splitcompat.SplitCompat
import com.hoanganhtuan95ptit.startapp.StartApp

class App: Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        SplitCompat.install(this) // quan trọng
    }

    override fun onCreate() {
        super.onCreate()

        StartApp.init(this)
    }
}