package com.stringconcat.ddd.shop.usecase.order

import arrow.core.Either
import com.stringconcat.ddd.shop.domain.order.ShopOrderId

interface CompleteOrder {
    fun execute(orderId: ShopOrderId): Either<CompleteOrderUseCaseError, Unit>
}

sealed class CompleteOrderUseCaseError {
    object OrderNotFound : CompleteOrderUseCaseError()
    object InvalidOrderState : CompleteOrderUseCaseError()
}
