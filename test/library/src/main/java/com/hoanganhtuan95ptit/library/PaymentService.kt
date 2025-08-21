package com.hoanganhtuan95ptit.library

import android.util.Log

interface PaymentService {

    fun print() {
        Log.d("tuanha", "print: ${this.javaClass.simpleName}")
    }
}