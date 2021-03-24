package com.stringconcat.ddd.shop.domain.order

import com.stringconcat.ddd.shop.domain.cart.CustomerId

interface CustomerHasActiveOrder {
    fun check(forCustomer: CustomerId): Boolean
}