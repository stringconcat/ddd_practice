package com.stringconcat.ddd.kitchen.usecase.order

import com.stringconcat.ddd.kitchen.usecase.order.dto.OrderDetails

interface GetOrders {

    fun execute(): List<OrderDetails>
}