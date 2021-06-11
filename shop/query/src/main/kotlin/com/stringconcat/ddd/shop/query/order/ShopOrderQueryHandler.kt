package com.stringconcat.ddd.shop.query.order

interface ShopOrderQueryHandler {

    fun getAll(): List<ShopOrderInfo>
}