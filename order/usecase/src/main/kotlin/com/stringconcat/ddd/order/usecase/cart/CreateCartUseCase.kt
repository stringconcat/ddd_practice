package com.stringconcat.ddd.order.usecase.cart

import com.stringconcat.ddd.order.domain.cart.CartFactory
import com.stringconcat.ddd.order.domain.cart.CartId
import com.stringconcat.ddd.order.domain.cart.CustomerId

class CreateCartUseCase(
    private val cartFactory: CartFactory,
    private val cartPersister: CartPersister
) {
    fun createOrGetCart(customerId: String): CartId {
        val cart = cartFactory.createOrGetCart(forCustomer = CustomerId(customerId))
        cartPersister.save(cart)
        return cart.id
    }
}