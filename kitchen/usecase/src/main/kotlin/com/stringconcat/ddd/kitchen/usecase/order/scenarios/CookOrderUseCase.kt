package com.stringconcat.ddd.kitchen.usecase.order.scenarios

import arrow.core.Either
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderId
import com.stringconcat.ddd.kitchen.usecase.order.CookOrder
import com.stringconcat.ddd.kitchen.usecase.order.CookOrderUseCaseError
import com.stringconcat.ddd.kitchen.usecase.order.access.KitchenOrderExtractor
import com.stringconcat.ddd.kitchen.usecase.order.access.KitchenOrderPersister

class CookOrderUseCase(
    private val kitchenOrderExtractor: KitchenOrderExtractor,
    private val kitchenOrderPersister: KitchenOrderPersister
) : CookOrder {

    override fun execute(orderId: KitchenOrderId): Either<CookOrderUseCaseError, Unit> {
        return kitchenOrderExtractor.getById(orderId)
            .rightIfNotNull { CookOrderUseCaseError.OrderNotFound }
            .map { order ->
                order.cook()
                kitchenOrderPersister.save(order)
            }
    }
}