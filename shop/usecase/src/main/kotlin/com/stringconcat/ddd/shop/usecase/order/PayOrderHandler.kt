package com.stringconcat.ddd.shop.usecase.order

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.shop.domain.order.CustomerOrderId

class PayOrderHandler(
    private val customerOrderExtractor: CustomerOrderExtractor,
    private val customerOrderPersister: CustomerOrderPersister
) : PayOrder {

    override fun execute(orderId: CustomerOrderId): Either<PayOrderHandlerError, Unit> {
        return customerOrderExtractor.getById(orderId)
            .rightIfNotNull { PayOrderHandlerError.OrderNotFound }
            .flatMap { order ->
                order.pay().map {
                    customerOrderPersister.save(order)
                }.mapLeft { PayOrderHandlerError.InvalidOrderState }
            }
    }
}