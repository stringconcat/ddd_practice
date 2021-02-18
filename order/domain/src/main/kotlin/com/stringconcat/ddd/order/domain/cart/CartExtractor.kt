package com.stringconcat.ddd.order.domain.cart

interface CartExtractor {
    fun getCart(forCustomer: CustomerId): Cart?
}