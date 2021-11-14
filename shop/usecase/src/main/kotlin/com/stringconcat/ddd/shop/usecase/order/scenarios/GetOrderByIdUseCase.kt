package com.stringconcat.ddd.shop.usecase.order.scenarios

import arrow.core.rightIfNotNull
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.usecase.order.GetOrderById
import com.stringconcat.ddd.shop.usecase.order.GetOrderByIdUseCaseError
import com.stringconcat.ddd.shop.usecase.order.access.ShopOrderExtractor
import com.stringconcat.ddd.shop.usecase.order.dto.toDetails

class GetOrderByIdUseCase(private val orderExtractor: ShopOrderExtractor) : GetOrderById {
    override fun execute(id: ShopOrderId) =
        orderExtractor.getById(id)
            .rightIfNotNull { GetOrderByIdUseCaseError.OrderNotFound }
            .map { it.toDetails() }
}
