package com.stringconcat.ddd.order.usecase.cart

import com.stringconcat.ddd.order.domain.cart.Cart
import com.stringconcat.ddd.order.domain.cart.CustomerId

interface CartExtractor {
    fun getCart(forCustomer: CustomerId): Cart?
}