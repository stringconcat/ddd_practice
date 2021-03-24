package com.stringconcat.ddd.shop.domain.order

data class CustomerOrderId(val value: Long)

interface CustomerOrderIdGenerator {
    fun generate(): CustomerOrderId
}