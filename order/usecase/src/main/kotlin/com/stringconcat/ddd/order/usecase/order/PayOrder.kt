package com.stringconcat.ddd.order.usecase.order

import arrow.core.Either
import com.stringconcat.ddd.order.domain.order.CustomerOrderId

interface PayOrder {
    fun execute(orderId: CustomerOrderId): Either<PayOrderHandlerError, Unit>
}

sealed class PayOrderHandlerError(val message: String) {
    object OrderNotFound : PayOrderHandlerError("Order not found")
    object InvalidOrderState : PayOrderHandlerError("Invalid order state")
}