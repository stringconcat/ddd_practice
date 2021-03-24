package com.stringconcat.ddd.shop.usecase.order

import arrow.core.Either
import com.stringconcat.ddd.order.domain.order.CustomerOrderId

interface ConfirmOrder {
    fun execute(orderId: CustomerOrderId): Either<ConfirmOrderUseCaseError, Unit>
}

sealed class ConfirmOrderUseCaseError(val message: String) {
    object OrderNotFound : ConfirmOrderUseCaseError("Order not found")
    object InvalidOrderState : ConfirmOrderUseCaseError("Invalid order state")
}