package com.stringconcat.ddd.shop.usecase.order

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.shop.domain.order.CustomerOrderId

class ConfirmOrderUseCase(
    private val customerOrderExtractor: CustomerOrderExtractor,
    private val customerOrderPersister: CustomerOrderPersister
) : ConfirmOrder {

    override fun execute(orderId: CustomerOrderId): Either<ConfirmOrderUseCaseError, Unit> {
        return customerOrderExtractor.getById(orderId)
            .rightIfNotNull { ConfirmOrderUseCaseError.OrderNotFound }
            .flatMap { order ->
                order.confirm().map {
                    customerOrderPersister.save(order)
                }.mapLeft { ConfirmOrderUseCaseError.InvalidOrderState }
            }
    }
}