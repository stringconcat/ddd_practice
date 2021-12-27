package com.stringconcat.ddd.kitchen.usecase.order.scenarios

import com.stringconcat.ddd.kitchen.usecase.order.GetOrders
import com.stringconcat.ddd.kitchen.usecase.order.access.KitchenOrderExtractor
import com.stringconcat.ddd.kitchen.usecase.order.dto.toDetails

class GetOrdersUseCase(private val orderExtractor: KitchenOrderExtractor) : GetOrders {
    override fun execute() = orderExtractor.getAll().map { it.toDetails() }
}