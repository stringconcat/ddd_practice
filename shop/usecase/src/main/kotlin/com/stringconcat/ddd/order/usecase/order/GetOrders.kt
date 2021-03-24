package com.stringconcat.ddd.order.usecase.order

import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.order.domain.menu.Price
import com.stringconcat.ddd.order.domain.order.CustomerOrderId
import com.stringconcat.ddd.order.domain.order.OrderState

interface GetOrders {
    fun execute(): List<CustomerOrderInfo>
}

data class CustomerOrderInfo(
    val id: CustomerOrderId,
    val state: OrderState,
    val address: Address,
    val total: Price
)