package com.stringconcat.ddd.order.usecase.order

import arrow.core.Either

interface PayOrder {
    fun execute(orderId: Long): Either<PayOrderHandlerError, Unit>
}

sealed class PayOrderHandlerError {
    object OrderNotFound : PayOrderHandlerError()
    object InvalidOrderState : PayOrderHandlerError()
}