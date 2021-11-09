package com.stringconcat.ddd.shop.usecase.cart.access

import com.stringconcat.ddd.shop.domain.cart.Cart

interface CartPersister {
    fun save(cart: Cart)
}