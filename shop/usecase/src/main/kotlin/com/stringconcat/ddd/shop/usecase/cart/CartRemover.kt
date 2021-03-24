package com.stringconcat.ddd.shop.usecase.cart

import com.stringconcat.ddd.shop.domain.cart.Cart

interface CartRemover {
    fun deleteCart(cart: Cart)
}