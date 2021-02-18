package com.stringconcat.ddd.order.usecase.cart

import com.stringconcat.ddd.order.domain.cart.Cart

interface CartPersister {
    fun save(cart: Cart)
}