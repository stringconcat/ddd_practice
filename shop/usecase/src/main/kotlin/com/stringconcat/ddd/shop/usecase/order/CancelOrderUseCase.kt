package com.stringconcat.ddd.shop.usecase.order

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.shop.domain.order.CustomerOrderId

class CancelOrderUseCase(
    private val customerOrderExtractor: CustomerOrderExtractor,
    private val customerOrderPersister: CustomerOrderPersister
) : CancelOrder {

    override fun execute(orderId: CustomerOrderId): Either<CancelOrderUseCaseError, Unit> {
        return customerOrderExtractor.getById(orderId)
            .rightIfNotNull { CancelOrderUseCaseError.OrderNotFound }
            .flatMap { order ->
                order.cancel().map {
                    customerOrderPersister.save(order)
                }.mapLeft { CancelOrderUseCaseError.InvalidOrderState }
            }
    }
}