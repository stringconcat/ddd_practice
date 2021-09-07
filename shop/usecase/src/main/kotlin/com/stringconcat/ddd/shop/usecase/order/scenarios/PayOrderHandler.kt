package com.stringconcat.ddd.shop.usecase.order.scenarios

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.usecase.order.PayOrder
import com.stringconcat.ddd.shop.usecase.order.PayOrderHandlerError

class PayOrderHandler(
    private val shopOrderExtractor: ShopOrderExtractor,
    private val shopOrderPersister: ShopOrderPersister
) : PayOrder {

    override fun execute(orderId: ShopOrderId): Either<PayOrderHandlerError, Unit> {
        return shopOrderExtractor.getById(orderId)
            .rightIfNotNull { PayOrderHandlerError.OrderNotFound }
            .flatMap { order ->
                order.pay().map {
                    shopOrderPersister.save(order)
                }.mapLeft { PayOrderHandlerError.InvalidOrderState }
            }
    }
}