package com.stringconcat.dev.course.app.listeners

import com.stringconcat.ddd.order.domain.payment.OrderPayment

interface PaymentExporter {

    fun exportPayment(payment: OrderPayment)
}
