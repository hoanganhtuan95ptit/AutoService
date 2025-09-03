package com.hoanganhtuan95ptit.example

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.hoanganhtuan95ptit.autobind.AutoBind
import com.hoanganhtuan95ptit.library.PaymentService
import com.hoanganhtuan95ptit.startapp.StartApp
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {

            StartApp.activityFlow.mapNotNull { it }.collect {
                Log.d("tuanha", "onCreate: ${it.javaClass.name}")
            }
        }

        lifecycleScope.launch {

            AutoBind.loadAsync(PaymentService::class.java, true).collect { list ->
                list.map { it.print() }
            }
        }

        val manager = SplitInstallManagerFactory.create(this)

        val request = SplitInstallRequest.newBuilder()
            .addModule("dynamicfeature")
            .build()

        manager.startInstall(request).addOnSuccessListener { sessionId ->

        }.addOnFailureListener { e ->

        }
    }
}