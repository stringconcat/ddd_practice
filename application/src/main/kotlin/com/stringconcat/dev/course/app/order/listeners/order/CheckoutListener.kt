package com.stringconcat.dev.course.app.order.listeners.order

import com.stringconcat.ddd.order.domain.order.CustomerOrderCreatedDomainEvent
import com.stringconcat.ddd.order.usecase.cart.RemoveCartHandler
import com.stringconcat.dev.course.app.event.DomainEventListener
import org.slf4j.LoggerFactory

class CheckoutListener(
    private val removeCartHandler: RemoveCartHandler,
) : DomainEventListener<CustomerOrderCreatedDomainEvent> {

    private val logger = LoggerFactory.getLogger(CheckoutListener::class.java)

    override fun eventType() = CustomerOrderCreatedDomainEvent::class

    override fun handle(event: CustomerOrderCreatedDomainEvent) {
        val result = removeCartHandler.removeCart(event.customerId)
        result.mapLeft {
            logger.warn("Cart for customer #${event.customerId} is already removed")
        }
    }
}