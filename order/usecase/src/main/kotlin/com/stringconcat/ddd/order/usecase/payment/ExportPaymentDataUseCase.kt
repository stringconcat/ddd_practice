package com.stringconcat.ddd.order.usecase.payment

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.order.domain.order.CustomerOrderId
import com.stringconcat.ddd.order.domain.order.OrderState
import com.stringconcat.ddd.order.domain.payment.OrderPayment
import com.stringconcat.ddd.order.usecase.order.CustomerOrderExtractor

class ExportPaymentDataUseCase(
    private val paymentExporter: PaymentExporter,
    private val orderExtractor: CustomerOrderExtractor
) : ExportPaymentData {

    override fun execute(orderId: CustomerOrderId): Either<ExportPaymentDataError, Unit> {
        val order = orderExtractor.getById(orderId) ?: return ExportPaymentDataError.OrderNotFound.left()
        if (!order.isPaid()) {
            return ExportPaymentDataError.InvalidOrderState.left()
        }

        val orderPayment = OrderPayment(orderId = order.id, price = order.totalPrice())
        paymentExporter.exportPayment(payment = orderPayment)
        return Unit.right()
    }
}
