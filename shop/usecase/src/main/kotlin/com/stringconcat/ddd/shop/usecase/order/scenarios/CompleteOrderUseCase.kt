package com.stringconcat.ddd.shop.usecase.order.scenarios

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.usecase.order.CompleteOrder
import com.stringconcat.ddd.shop.usecase.order.CompleteOrderUseCaseError

class CompleteOrderUseCase(
    private val shopOrderExtractor: ShopOrderExtractor,
    private val shopOrderPersister: ShopOrderPersister
) : CompleteOrder {

    override fun execute(orderId: ShopOrderId): Either<CompleteOrderUseCaseError, Unit> {
        return shopOrderExtractor.getById(orderId)
            .rightIfNotNull { CompleteOrderUseCaseError.OrderNotFound }
            .flatMap { order ->
                order.complete().map {
                    shopOrderPersister.save(order)
                }.mapLeft { CompleteOrderUseCaseError.InvalidOrderState }
            }
    }
}