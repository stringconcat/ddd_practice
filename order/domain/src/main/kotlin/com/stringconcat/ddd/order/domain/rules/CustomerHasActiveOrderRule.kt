package com.stringconcat.ddd.order.domain.rules

import com.stringconcat.ddd.order.domain.cart.CustomerId

interface CustomerHasActiveOrderRule {
    fun hasActiveOrder(customerId: CustomerId): Boolean
}