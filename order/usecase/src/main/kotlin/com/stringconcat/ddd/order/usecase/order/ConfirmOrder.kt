package com.stringconcat.ddd.order.usecase.order

import arrow.core.Either

interface ConfirmOrder {
    fun execute(orderId: Long): Either<ConfirmOrderUseCaseError, Unit>
}

sealed class ConfirmOrderUseCaseError {
    object OrderNotFound : ConfirmOrderUseCaseError()
    object InvalidOrderState : ConfirmOrderUseCaseError()
}