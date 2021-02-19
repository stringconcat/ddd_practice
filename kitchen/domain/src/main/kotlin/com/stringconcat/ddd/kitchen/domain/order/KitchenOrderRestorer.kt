package com.stringconcat.ddd.kitchen.domain.order

import com.stringconcat.ddd.common.types.base.Version

object KitchenOrderRestorer {

    fun restoreOrder(
        id: KitchenOrderId,
        orderItems: List<OrderItem>,
        cooked: Boolean,
        version: Version
    ): KitchenOrder {
        return KitchenOrder(
            id = id,
            orderItems = orderItems,
            version = version
        ).apply {
            this.cooked = cooked
        }
    }
}