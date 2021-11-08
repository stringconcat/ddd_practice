package com.stringconcat.ddd.shop.usecase.cart.scenarios

import arrow.core.rightIfNotNull
import com.stringconcat.ddd.common.events.DomainEventListener
import com.stringconcat.ddd.shop.domain.order.ShopOrderCreatedDomainEvent
import org.slf4j.LoggerFactory

class RemoveCartAfterCheckoutRule(
    private val cartExtractor: CartExtractor,
    private val cartRemover: CartRemover
) : DomainEventListener<ShopOrderCreatedDomainEvent> {

    private val logger = LoggerFactory.getLogger(RemoveCartAfterCheckoutRule::class.java)

    override fun eventType() = ShopOrderCreatedDomainEvent::class

    override fun handle(event: ShopOrderCreatedDomainEvent) {
        cartExtractor.getCart(event.forCustomer).rightIfNotNull {
            logger.warn("Cart for customer #${event.forCustomer} is already removed")
        }.map {
            // тут можно не делать никаких методов в самой коризине, потому что
            // корзина никому не интересна с точки зрения процессов
            cartRemover.deleteCart(it)
        }
    }
}