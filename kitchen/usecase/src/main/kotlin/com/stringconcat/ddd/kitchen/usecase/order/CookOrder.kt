package com.stringconcat.ddd.kitchen.usecase.order

import arrow.core.Either

interface CookOrder {
    fun execute(orderId: Long): Either<CookOrderUseCaseError, Unit>
}

sealed class CookOrderUseCaseError {
    object OrderNotFound : CookOrderUseCaseError()
}