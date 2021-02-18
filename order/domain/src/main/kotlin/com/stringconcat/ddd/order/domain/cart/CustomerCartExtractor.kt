package com.stringconcat.ddd.order.domain.cart

interface CustomerCartExtractor {
    fun getCart(forCustomer: CustomerId): Cart?
}