package com.stringconcat.ddd.order.persistence.cart

import com.stringconcat.ddd.common.types.base.EventPublisher
import com.stringconcat.ddd.order.domain.cart.Cart
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.usecase.cart.CartExtractor
import com.stringconcat.ddd.order.usecase.cart.CartPersister
import com.stringconcat.ddd.order.usecase.cart.CartRemover

class InMemoryCartRepository(private val eventPublisher: EventPublisher) : CartExtractor, CartPersister, CartRemover {

    internal val storage = HashMap<CustomerId, Cart>()

    override fun getCart(forCustomer: CustomerId) = storage[forCustomer]

    override fun save(cart: Cart) {
        eventPublisher.publish(cart.popEvents())
        storage[cart.forCustomer] = cart
    }

    override fun deleteCart(cart: Cart) {
        storage.remove(cart.forCustomer)
    }
}