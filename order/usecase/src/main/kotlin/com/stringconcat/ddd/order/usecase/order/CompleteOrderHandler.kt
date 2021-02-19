package com.stringconcat.ddd.order.usecase.order

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.order.domain.order.CustomerOrderId

class CompleteOrderHandler(
    private val customerOrderExtractor: CustomerOrderExtractor,
    private val customerOrderPersister: CustomerOrderPersister
) {

    fun completeOrder(orderId: Long): Either<CompleteOrderHandlerError, Unit> {
        return customerOrderExtractor.getById(CustomerOrderId(orderId))
            .rightIfNotNull { CompleteOrderHandlerError.OrderNotFound }
            .flatMap { order ->
                order.complete().map {
                    customerOrderPersister.save(order)
                }.mapLeft { CompleteOrderHandlerError.InvalidOrderState }
            }
    }
}

sealed class CompleteOrderHandlerError {
    object OrderNotFound : CompleteOrderHandlerError()
    object InvalidOrderState : CompleteOrderHandlerError()
}