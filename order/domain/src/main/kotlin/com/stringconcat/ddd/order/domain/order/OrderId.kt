package com.stringconcat.ddd.order.domain.order

data class OrderId(val value: Long)

interface OrderIdGenerator {
    fun generate(): OrderId
}