package com.stringconcat.ddd.shop.usecase.order

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.shop.domain.order.ShopOrderId

class CancelOrderUseCase(
    private val shopOrderExtractor: ShopOrderExtractor,
    private val shopOrderPersister: ShopOrderPersister
) : CancelOrder {

    override fun execute(orderId: ShopOrderId): Either<CancelOrderUseCaseError, Unit> {
        return shopOrderExtractor.getById(orderId)
            .rightIfNotNull { CancelOrderUseCaseError.OrderNotFound }
            .flatMap { order ->
                order.cancel().map {
                    shopOrderPersister.save(order)
                }.mapLeft { CancelOrderUseCaseError.InvalidOrderState }
            }
    }
}