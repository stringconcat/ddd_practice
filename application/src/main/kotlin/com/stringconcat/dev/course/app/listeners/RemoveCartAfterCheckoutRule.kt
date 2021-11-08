package com.stringconcat.dev.course.app.listeners

import com.stringconcat.ddd.common.events.DomainEventListener
import com.stringconcat.ddd.shop.domain.order.ShopOrderCreatedDomainEvent
import com.stringconcat.ddd.shop.usecase.cart.RemoveCart
import org.slf4j.LoggerFactory

class RemoveCartAfterCheckoutRule(
    private val removeCart: RemoveCart,
) : DomainEventListener<ShopOrderCreatedDomainEvent> {

    private val logger = LoggerFactory.getLogger(RemoveCartAfterCheckoutRule::class.java)

    override fun eventType() = ShopOrderCreatedDomainEvent::class

    override fun handle(event: ShopOrderCreatedDomainEvent) {
        val result = removeCart.execute(event.forCustomer)
        result.mapLeft {
            logger.warn("Cart for customer #${event.forCustomer} is already removed")
        }
    }
}