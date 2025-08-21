package com.hoanganhtuan95ptit.dynamicfeature

import com.hoanganhtuan95ptit.autobind.annotation.AutoBind
import com.hoanganhtuan95ptit.library.PaymentService

@AutoBind(PaymentService::class)
class DynamicPaymentService : PaymentService