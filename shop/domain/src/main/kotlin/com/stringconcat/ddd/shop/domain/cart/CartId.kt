package com.stringconcat.ddd.shop.domain.cart

data class CartId(private val value: Long) {
    fun toLongValue() = value
}

interface CartIdGenerator {
    fun generate(): CartId
}