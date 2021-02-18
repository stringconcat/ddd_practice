package com.stringconcat.ddd.order.domain.cart

interface CustomerCartExtractor {
    fun getCartByCustomerId(customerId: CustomerId): Cart?
}