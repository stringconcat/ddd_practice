package com.stringconcat.ddd.shop.domain.cart

data class CartId(val value: Long)

interface CartIdGenerator {
    fun generate(): CartId
}