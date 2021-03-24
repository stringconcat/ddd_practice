package com.stringconcat.ddd.shop.usecase.order

import arrow.core.Either
import com.stringconcat.ddd.shop.domain.order.ShopOrderId

interface CancelOrder {
    fun execute(orderId: ShopOrderId): Either<CancelOrderUseCaseError, Unit>
}

sealed class CancelOrderUseCaseError(val message: String) {
    object OrderNotFound : CancelOrderUseCaseError("Order not found")
    object InvalidOrderState : CancelOrderUseCaseError("Invalid order state")
}