package com.stringconcat.ddd.order.domain.cart

data class CartId(val value: Long)

interface CartIdGenerator {
    fun generate(): CartId
}