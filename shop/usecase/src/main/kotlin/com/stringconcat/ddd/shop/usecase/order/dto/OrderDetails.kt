package com.stringconcat.ddd.shop.usecase.order.dto

import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.domain.order.OrderState
import com.stringconcat.ddd.shop.domain.order.ShopOrder
import com.stringconcat.ddd.shop.domain.order.ShopOrderId

data class OrderDetails(
    val id: ShopOrderId,
    val state: OrderState,
    val address: Address,
    val readyForConfirmOrCancel: Boolean,
    val items: List<OrderItemDetails>,
    val total: Price,
    val version: Version,
)

fun ShopOrder.toDetails(): OrderDetails {
    val items =
        this.orderItems
            .map { OrderItemDetails(mealId = it.mealId, count = it.count) }
    return OrderDetails(
        id = this.id,
        items = items,
        total = this.totalPrice(),
        state = this.state,
        readyForConfirmOrCancel = this.readyForConfirmOrCancel(),
        address = this.address,
        version = this.version
    )
}

data class OrderItemDetails(
    val mealId: MealId,
    val count: Count,
)