package com.stringconcat.ddd.shop.domain.order

data class ShopOrderId(private val value: Long) {
    fun toLongValue() = value
}

interface ShopOrderIdGenerator {
    fun generate(): ShopOrderId
}