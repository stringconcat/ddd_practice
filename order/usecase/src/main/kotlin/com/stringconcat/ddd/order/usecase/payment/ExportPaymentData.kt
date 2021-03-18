package com.stringconcat.ddd.order.usecase.payment

import arrow.core.Either
import com.stringconcat.ddd.order.domain.order.CustomerOrderId

interface ExportPaymentData {

    fun execute(orderId: CustomerOrderId): Either<ExportPaymentDataError, Unit>
}

sealed class ExportPaymentDataError(val message: String) {
    object OrderNotFound : ExportPaymentDataError("Order not found")
    object InvalidOrderState : ExportPaymentDataError("Invalid order state")
}
