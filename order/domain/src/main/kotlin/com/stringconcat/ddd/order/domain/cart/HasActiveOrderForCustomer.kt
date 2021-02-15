package com.stringconcat.ddd.order.domain.cart

interface HasActiveOrderForCustomer {
    fun hasActiveOrder(customerId: CustomerId): Boolean
}