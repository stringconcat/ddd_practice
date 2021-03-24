package com.stringconcat.ddd.shop.usecase.order

import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.domain.order.CustomerOrderId
import com.stringconcat.ddd.shop.domain.order.OrderState

interface GetOrders {
    fun execute(): List<CustomerOrderInfo>
}

data class CustomerOrderInfo(
    val id: CustomerOrderId,
    val state: OrderState,
    val address: Address,
    val total: Price
)