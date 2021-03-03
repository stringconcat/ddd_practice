package com.stringconcat.ddd.order.domain.order

import com.stringconcat.ddd.order.domain.cart.CustomerId

interface CustomerHasActiveOrder {
    fun check(forCustomer: CustomerId): Boolean
}