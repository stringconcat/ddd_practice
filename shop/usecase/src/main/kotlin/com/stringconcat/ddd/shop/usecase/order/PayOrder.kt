package com.stringconcat.ddd.shop.usecase.order

import arrow.core.Either
import com.stringconcat.ddd.shop.domain.order.ShopOrderId

interface PayOrder {
    fun execute(orderId: ShopOrderId): Either<PayOrderHandlerError, Unit>
}

sealed class PayOrderHandlerError(val message: String) {
    object OrderNotFound : PayOrderHandlerError("Order not found")
    object InvalidOrderState : PayOrderHandlerError("Invalid order state")
}