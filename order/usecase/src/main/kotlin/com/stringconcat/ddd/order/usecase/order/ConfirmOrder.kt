package com.stringconcat.ddd.order.usecase.order

import arrow.core.Either
import com.stringconcat.ddd.order.domain.order.CustomerOrderId

interface ConfirmOrder {
    fun execute(orderId: CustomerOrderId): Either<ConfirmOrderUseCaseError, Unit>
}

sealed class ConfirmOrderUseCaseError {
    object OrderNotFound : ConfirmOrderUseCaseError()
    object InvalidOrderState : ConfirmOrderUseCaseError()
}