package com.stringconcat.ddd.shop.domain.order

data class ShopOrderId(val value: Long)

interface ShopOrderIdGenerator {
    fun generate(): ShopOrderId
}