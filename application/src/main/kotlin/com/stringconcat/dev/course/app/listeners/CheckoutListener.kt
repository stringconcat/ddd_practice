package com.stringconcat.dev.course.app.listeners

import com.stringconcat.ddd.order.domain.order.CustomerOrderCreatedDomainEvent
import com.stringconcat.ddd.order.usecase.cart.RemoveCartHandler
import com.stringconcat.dev.course.app.event.DomainEventListener
import org.apache.logging.log4j.kotlin.Logging

class CheckoutListener(
    private val removeCartHandler: RemoveCartHandler,
) : DomainEventListener<CustomerOrderCreatedDomainEvent> {

    companion object : Logging

    override fun eventType() = CustomerOrderCreatedDomainEvent::class

    override fun handle(event: CustomerOrderCreatedDomainEvent) {
        val result = removeCartHandler.removeCart(event.customerId)
        result.mapLeft {
            logger.warn { "Cart for customer #${event.customerId} is already removed" }
        }
    }
}