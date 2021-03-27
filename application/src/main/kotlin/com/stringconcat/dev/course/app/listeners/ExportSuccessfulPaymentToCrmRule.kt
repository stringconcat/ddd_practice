package com.stringconcat.dev.course.app.listeners

import com.stringconcat.ddd.order.domain.order.CustomerOrderHasBeenPaidDomainEvent
import com.stringconcat.ddd.order.usecase.payment.ExportPaymentData
import com.stringconcat.dev.course.app.event.DomainEventListener

class ExportSuccessfulPaymentToCrmRule(
    private val exportPaymentData: ExportPaymentData
) : DomainEventListener<CustomerOrderHasBeenPaidDomainEvent> {

    override fun eventType() = CustomerOrderHasBeenPaidDomainEvent::class

    override fun handle(event: CustomerOrderHasBeenPaidDomainEvent) {
        exportPaymentData.execute(event.orderId).mapLeft { error ->
            throw error(error.message)
        }
    }
}
