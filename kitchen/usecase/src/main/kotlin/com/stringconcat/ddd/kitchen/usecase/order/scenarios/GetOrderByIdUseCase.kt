package com.stringconcat.ddd.kitchen.usecase.order.scenarios

import arrow.core.rightIfNotNull
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderId
import com.stringconcat.ddd.kitchen.usecase.order.GetOrderById
import com.stringconcat.ddd.kitchen.usecase.order.GetOrderByIdUseCaseError
import com.stringconcat.ddd.kitchen.usecase.order.access.KitchenOrderExtractor
import com.stringconcat.ddd.kitchen.usecase.order.dto.toDetails

class GetOrderByIdUseCase(private val orderExtractor: KitchenOrderExtractor) : GetOrderById {
    override fun execute(id: KitchenOrderId) =
        orderExtractor.getById(id)
            .rightIfNotNull { GetOrderByIdUseCaseError.OrderNotFound }
            .map { it.toDetails() }
}