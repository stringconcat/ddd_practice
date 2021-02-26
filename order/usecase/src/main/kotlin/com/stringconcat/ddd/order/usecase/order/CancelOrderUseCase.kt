package com.stringconcat.ddd.order.usecase.order

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.order.domain.order.CustomerOrderId

class CancelOrderUseCase(
    private val customerOrderExtractor: CustomerOrderExtractor,
    private val customerOrderPersister: CustomerOrderPersister
) : CancelOrder {

    override fun execute(orderId: Long): Either<CancelOrderUseCaseError, Unit> {
        return customerOrderExtractor.getById(CustomerOrderId(orderId))
            .rightIfNotNull { CancelOrderUseCaseError.OrderNotFound }
            .flatMap { order ->
                order.cancel().map {
                    customerOrderPersister.save(order)
                }.mapLeft { CancelOrderUseCaseError.InvalidOrderState }
            }
    }
}