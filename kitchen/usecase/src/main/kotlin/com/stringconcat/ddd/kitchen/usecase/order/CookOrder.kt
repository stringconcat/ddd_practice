package com.stringconcat.ddd.kitchen.usecase.order

import arrow.core.Either
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderId

interface CookOrder {
    fun execute(orderId: KitchenOrderId): Either<CookOrderUseCaseError, Unit>
}

sealed class CookOrderUseCaseError {
    object OrderNotFound : CookOrderUseCaseError()
}