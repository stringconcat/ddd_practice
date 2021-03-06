package com.stringconcat.ddd.shop.persistence.cart

import com.stringconcat.ddd.common.types.base.EventPublisher
import com.stringconcat.ddd.shop.domain.cart.Cart
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.usecase.cart.CartExtractor
import com.stringconcat.ddd.shop.usecase.cart.CartPersister
import com.stringconcat.ddd.shop.usecase.cart.CartRemover

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