package com.stringconcat.ddd.shop.usecase.order.scenarios

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.usecase.order.ConfirmOrder
import com.stringconcat.ddd.shop.usecase.order.ConfirmOrderUseCaseError

class ConfirmOrderUseCase(
    private val shopOrderExtractor: ShopOrderExtractor,
    private val shopOrderPersister: ShopOrderPersister
) : ConfirmOrder {

    override fun execute(orderId: ShopOrderId): Either<ConfirmOrderUseCaseError, Unit> {
        return shopOrderExtractor.getById(orderId)
            .rightIfNotNull { ConfirmOrderUseCaseError.OrderNotFound }
            .flatMap { order ->
                order.confirm().map {
                    shopOrderPersister.save(order)
                }.mapLeft { ConfirmOrderUseCaseError.InvalidOrderState }
            }
    }
}