package com.stringconcat.ddd.order.usecase.order

import arrow.core.Either

interface CompleteOrder {
    fun execute(orderId: Long): Either<CompleteOrderUseCaseError, Unit>
}

sealed class CompleteOrderUseCaseError {
    object OrderNotFound : CompleteOrderUseCaseError()
    object InvalidOrderState : CompleteOrderUseCaseError()
}
