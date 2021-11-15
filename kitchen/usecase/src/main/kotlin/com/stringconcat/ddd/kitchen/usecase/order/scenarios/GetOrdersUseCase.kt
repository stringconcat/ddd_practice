package com.stringconcat.ddd.kitchen.usecase.order.scenarios

import com.stringconcat.ddd.kitchen.usecase.order.GetOrders
import com.stringconcat.ddd.kitchen.usecase.order.access.KitchenOrderExtractor
import com.stringconcat.ddd.kitchen.usecase.order.dto.OrderDetails

class GetOrdersUseCase(private val orderExtractor: KitchenOrderExtractor) : GetOrders {

    override fun execute(): List<OrderDetails> {
        return orderExtractor.getAll().map {
            OrderDetails(
                id = it.id,
                meals = it.meals,
                cooked = it.cooked
            )
        }.toList()
    }
}