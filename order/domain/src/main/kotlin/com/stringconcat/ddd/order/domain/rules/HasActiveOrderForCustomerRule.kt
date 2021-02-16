package com.stringconcat.ddd.order.domain.rules

import com.stringconcat.ddd.order.domain.cart.CustomerId

interface HasActiveOrderForCustomerRule {
    fun hasActiveOrder(customerId: CustomerId): Boolean
}