package com.stringconcat.ddd.order.usecase.order

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.order.domain.order.CustomerOrderId

class CompleteOrderUseCase(
    private val customerOrderExtractor: CustomerOrderExtractor,
    private val customerOrderPersister: CustomerOrderPersister
) : CompleteOrder {

    override fun execute(orderId: Long): Either<CompleteOrderUseCaseError, Unit> {
        return customerOrderExtractor.getById(CustomerOrderId(orderId))
            .rightIfNotNull { CompleteOrderUseCaseError.OrderNotFound }
            .flatMap { order ->
                order.complete().map {
                    customerOrderPersister.save(order)
                }.mapLeft { CompleteOrderUseCaseError.InvalidOrderState }
            }
    }
}