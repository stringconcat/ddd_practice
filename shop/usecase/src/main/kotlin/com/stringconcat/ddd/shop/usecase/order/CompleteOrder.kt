package com.stringconcat.ddd.shop.usecase.order

import arrow.core.Either
import com.stringconcat.ddd.order.domain.order.CustomerOrderId

interface CompleteOrder {
    fun execute(orderId: CustomerOrderId): Either<CompleteOrderUseCaseError, Unit>
}

sealed class CompleteOrderUseCaseError {
    object OrderNotFound : CompleteOrderUseCaseError()
    object InvalidOrderState : CompleteOrderUseCaseError()
}
