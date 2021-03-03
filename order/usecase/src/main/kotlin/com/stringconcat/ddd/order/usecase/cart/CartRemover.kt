package com.stringconcat.ddd.order.usecase.cart

import com.stringconcat.ddd.order.domain.cart.Cart

interface CartRemover {
    fun deleteCart(cart: Cart)
}