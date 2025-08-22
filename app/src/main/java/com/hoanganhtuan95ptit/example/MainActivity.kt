package com.hoanganhtuan95ptit.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hoanganhtuan95ptit.autobind.AutoBind
import com.hoanganhtuan95ptit.library.PaymentService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AutoBind.init(application)
        AutoBind.load(PaymentService::class.java).map { it.print() }
    }
}