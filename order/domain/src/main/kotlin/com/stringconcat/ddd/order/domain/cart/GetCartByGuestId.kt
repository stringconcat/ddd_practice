package com.stringconcat.ddd.order.domain.cart

interface GetCartByGuestId {
    fun getCartByGuestId(customerId: CustomerId): Cart?
}