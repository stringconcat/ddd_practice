package com.stringconcat.ddd.order.usecase.order

import arrow.core.Either

interface CancelOrder {
    fun execute(orderId: Long): Either<CancelOrderUseCaseError, Unit>
}

sealed class CancelOrderUseCaseError {
    object OrderNotFound : CancelOrderUseCaseError()
    object InvalidOrderState : CancelOrderUseCaseError()
}