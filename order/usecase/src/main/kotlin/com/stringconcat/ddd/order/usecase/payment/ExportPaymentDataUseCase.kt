package com.stringconcat.ddd.order.usecase.payment

import arrow.core.Either
import arrow.core.filterOrElse
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import arrow.core.right
import com.stringconcat.ddd.order.domain.order.CustomerOrder
import com.stringconcat.ddd.order.domain.order.CustomerOrderId
import com.stringconcat.ddd.order.domain.payment.OrderPayment
import com.stringconcat.ddd.order.usecase.order.CustomerOrderExtractor

class ExportPaymentDataUseCase(
    private val paymentExporter: PaymentExporter,
    private val orderExtractor: CustomerOrderExtractor
) : ExportPaymentData {

    override fun execute(orderId: CustomerOrderId): Either<ExportPaymentDataError, Unit> {
        return orderExtractor.getById(orderId)
            .rightIfNotNull { ExportPaymentDataError.OrderNotFound }
            .filterOrElse(CustomerOrder::isPaid) { ExportPaymentDataError.InvalidOrderState }
            .flatMap { order ->
                val orderPayment = OrderPayment(orderId = order.id, price = order.totalPrice())
                paymentExporter.exportPayment(payment = orderPayment)
                Unit.right()
            }
    }
}
