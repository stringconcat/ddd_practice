package com.stringconcat.ddd.order.usecase.payment

import com.stringconcat.ddd.order.domain.payment.OrderPayment

interface PaymentExporter {

    fun exportPayment(payment: OrderPayment)
}
