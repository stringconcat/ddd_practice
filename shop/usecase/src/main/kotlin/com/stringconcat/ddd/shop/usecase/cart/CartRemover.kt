package com.stringconcat.ddd.shop.usecase.cart

import com.stringconcat.ddd.order.domain.cart.Cart

interface CartRemover {
    fun deleteCart(cart: Cart)
}