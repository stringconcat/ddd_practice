package com.stringconcat.ddd.order.usecase.order

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.order.domain.order.CustomerOrderId

class PayOrderUseCase(
    private val customerOrderExtractor: CustomerOrderExtractor,
    private val customerOrderPersister: CustomerOrderPersister
) {

    fun payOrder(orderId: Long): Either<PayOrderUseCaseError, Unit> {
        return customerOrderExtractor.getById(CustomerOrderId(orderId))
            .rightIfNotNull { PayOrderUseCaseError.OrderNotFound }
            .flatMap { order ->
                order.pay().map {
                    customerOrderPersister.save(order)
                }.mapLeft { PayOrderUseCaseError.InvalidOrderState }
            }
    }
}

sealed class PayOrderUseCaseError {
    object OrderNotFound : PayOrderUseCaseError()
    object InvalidOrderState : PayOrderUseCaseError()
}