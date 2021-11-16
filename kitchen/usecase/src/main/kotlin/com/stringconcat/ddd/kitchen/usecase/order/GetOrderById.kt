package com.stringconcat.ddd.kitchen.usecase.order

import arrow.core.Either
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderId
import com.stringconcat.ddd.kitchen.usecase.order.dto.OrderDetails

interface GetOrderById {
    fun execute(id: KitchenOrderId): Either<GetOrderByIdUseCaseError, OrderDetails>
}

sealed class GetOrderByIdUseCaseError {
    object OrderNotFound : GetOrderByIdUseCaseError()
}