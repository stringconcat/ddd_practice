package com.stringconcat.dev.course.app.listeners

import com.stringconcat.ddd.order.domain.order.CustomerOrderHasBeenPaidDomainEvent
import com.stringconcat.ddd.order.domain.payment.OrderPayment
import com.stringconcat.ddd.order.usecase.order.CustomerOrderExtractor
import com.stringconcat.ddd.order.usecase.payment.PaymentExporter
import com.stringconcat.dev.course.app.event.DomainEventListener

class ExportSuccessfulPaymentToCrmRule(
    private val paymentExporter: PaymentExporter,
    private val orderExtractor: CustomerOrderExtractor
) : DomainEventListener<CustomerOrderHasBeenPaidDomainEvent> {

    override fun eventType() = CustomerOrderHasBeenPaidDomainEvent::class

    override fun handle(event: CustomerOrderHasBeenPaidDomainEvent) {
        val order = orderExtractor.getById(event.orderId)
        checkNotNull(order) {
            "Order ${event.orderId} not found"
        }

        val orderPayment = OrderPayment(orderId = order.id, price = order.totalPrice())
        paymentExporter.exportPayment(payment = orderPayment)
    }
}