package com.hoanganhtuan95ptit.example

import com.hoanganhtuan95ptit.autobind.annotation.AutoBind
import com.hoanganhtuan95ptit.library.PaymentService

@AutoBind(PaymentService::class)
class AppPaymentService : PaymentService