package com.stringconcat.dev.course.app.listeners

import com.stringconcat.ddd.shop.domain.order.CustomerOrderCreatedDomainEvent
import com.stringconcat.ddd.shop.usecase.cart.RemoveCart
import com.stringconcat.dev.course.app.event.DomainEventListener
import org.slf4j.LoggerFactory

class RemoveCartAfterCheckoutRule(
    private val removeCart: RemoveCart,
) : DomainEventListener<CustomerOrderCreatedDomainEvent> {

    private val logger = LoggerFactory.getLogger(RemoveCartAfterCheckoutRule::class.java)

    override fun eventType() = CustomerOrderCreatedDomainEvent::class

    override fun handle(event: CustomerOrderCreatedDomainEvent) {
        val result = removeCart.execute(event.forCustomer)
        result.mapLeft {
            logger.warn("Cart for customer #${event.forCustomer} is already removed")
        }
    }
}