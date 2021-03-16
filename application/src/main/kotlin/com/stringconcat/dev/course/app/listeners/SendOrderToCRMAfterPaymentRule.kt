package com.stringconcat.dev.course.app.listeners

import com.stringconcat.ddd.order.domain.order.CustomerOrderHasBeenDomainEvent
import com.stringconcat.ddd.order.usecase.order.CrmProvider
import com.stringconcat.ddd.order.usecase.order.CustomerOrderExtractor
import com.stringconcat.dev.course.app.event.DomainEventListener

class SendOrderToCRMAfterPaymentRule(
    private val customerOrderExtractor: CustomerOrderExtractor,
    private val crmProvider: CrmProvider
) : DomainEventListener<CustomerOrderHasBeenDomainEvent> {

    override fun eventType() = CustomerOrderHasBeenDomainEvent::class

    override fun handle(event: CustomerOrderHasBeenDomainEvent) {
        val order = customerOrderExtractor.getById(event.orderId)
        checkNotNull(order) {
            "Customer order #${event.orderId} not found"
        }

        val totalPrice = order.totalPrice()
        crmProvider.send(order.id, totalPrice).mapLeft {
            error("Cannot send order #${order.id} to crm")
        }
    }
}