package com.hoanganhtuan95ptit.example

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.hoanganhtuan95ptit.autobind.AutoBind
import com.hoanganhtuan95ptit.library.PaymentService
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AutoBind.init(application)

        val manager = SplitInstallManagerFactory.create(this)

        val request = SplitInstallRequest.newBuilder()
            .addModule("dynamicfeature")
            .build()


        val listener = SplitInstallStateUpdatedListener { state ->


            val status = state.status()

            Log.d("tuanha", "onCreate: $status")

            if (status == SplitInstallSessionStatus.INSTALLED) lifecycleScope.launch {

                AutoBind.forceLoad()

                AutoBind.awaitLoaded()

                AutoBind.loadAsync(PaymentService::class.java).collect {
                    it.map { it.print() }
                }
            }
        }

// Đăng ký listener
        manager.registerListener(listener)

        manager.startInstall(request).addOnSuccessListener { sessionId ->

            Log.d("DFM", "Install started: $sessionId")
        }.addOnFailureListener { e ->

            Log.e("DFM", "Install failed", e)
        }
    }
}