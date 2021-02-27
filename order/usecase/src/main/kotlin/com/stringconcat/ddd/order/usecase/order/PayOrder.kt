package com.stringconcat.ddd.order.usecase.order

import arrow.core.Either
import com.stringconcat.ddd.order.domain.order.CustomerOrderId

interface PayOrder {
    fun execute(orderId: CustomerOrderId): Either<PayOrderHandlerError, Unit>
}

sealed class PayOrderHandlerError {
    object OrderNotFound : PayOrderHandlerError()
    object InvalidOrderState : PayOrderHandlerError()
}