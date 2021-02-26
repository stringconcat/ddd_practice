package com.stringconcat.ddd.order.usecase.order

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.order.domain.order.CustomerOrderId

class PayOrderHandler(
    private val customerOrderExtractor: CustomerOrderExtractor,
    private val customerOrderPersister: CustomerOrderPersister
) {

    fun execute(orderId: Long): Either<PayOrderHandlerError, Unit> {
        return customerOrderExtractor.getById(CustomerOrderId(orderId))
            .rightIfNotNull { PayOrderHandlerError.OrderNotFound }
            .flatMap { order ->
                order.pay().map {
                    customerOrderPersister.save(order)
                }.mapLeft { PayOrderHandlerError.InvalidOrderState }
            }
    }
}

sealed class PayOrderHandlerError {
    object OrderNotFound : PayOrderHandlerError()
    object InvalidOrderState : PayOrderHandlerError()
}