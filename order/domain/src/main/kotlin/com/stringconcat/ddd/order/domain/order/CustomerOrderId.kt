package com.stringconcat.ddd.order.domain.order

data class CustomerOrderId(val value: Long)

interface CustomerOrderIdGenerator {
    fun generate(): CustomerOrderId
}