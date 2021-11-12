package com.stringconcat.ddd.shop.usecase.order

import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.domain.order.OrderState

interface GetOrders {
    fun execute(startId: ShopOrderId): List<ShopOrderInfo>
}

data class ShopOrderInfo(
    val id: ShopOrderId,
    val state: OrderState,
    val address: Address,
    val total: Price
)