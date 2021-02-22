package com.stringconcat.ddd.order.persistence.cart

import com.stringconcat.ddd.common.types.base.EventPublisher
import com.stringconcat.ddd.order.domain.cart.Cart
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.usecase.cart.CartExtractor
import com.stringconcat.ddd.order.usecase.cart.CartPersister

class InMemoryCartRepository(private val eventPublisher: EventPublisher) : CartExtractor, CartPersister {

    internal val storage = HashMap<CustomerId, Cart>()

    override fun getCart(forCustomer: CustomerId) = storage[forCustomer]

    override fun save(cart: Cart) {
        eventPublisher.publish(cart.popEvents())
        storage[cart.customerId] = cart
    }
}