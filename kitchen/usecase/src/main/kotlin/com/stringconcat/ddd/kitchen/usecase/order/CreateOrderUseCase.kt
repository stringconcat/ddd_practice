package com.stringconcat.ddd.kitchen.usecase.order

import arrow.core.Either
import arrow.core.extensions.either.apply.tupled
import arrow.core.left
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.common.types.common.NegativeValueError
import com.stringconcat.ddd.kitchen.domain.order.EmptyMealNameError
import com.stringconcat.ddd.kitchen.domain.order.EmptyOrder
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrder
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderId
import com.stringconcat.ddd.kitchen.domain.order.Meal
import com.stringconcat.ddd.kitchen.domain.order.OrderItem

class CreateOrderUseCase(
    private val persister: KitchenOrderPersister
) {

    fun createOrder(request: CreateOrderRequest): Either<CreateOrderUseCaseError, Unit> {

        val orderId = KitchenOrderId(request.id)

        val items = request.items.map {
            tupled(
                transform(it.count),
                transform(it.mealName)
            ).map { sourceItem -> OrderItem(sourceItem.b, sourceItem.a) }
        }.map {
            it.mapLeft { e -> return@createOrder e.left() }
        }.mapNotNull { it.orNull() }

        return KitchenOrder.create(id = orderId, orderItems = items)
            .mapLeft { it.toError() }
            .map { order ->
                persister.save(order)
            }
    }

    private fun transform(count: Int): Either<CreateOrderUseCaseError, Count> {
        return Count.from(count).mapLeft { it.toError() }
    }

    private fun transform(mealName: String): Either<CreateOrderUseCaseError, Meal> {
        return Meal.from(mealName).mapLeft { it.toError() }
    }

}

data class CreateOrderRequest(val id: Long, val items: List<OrderItemData>) {
    data class OrderItemData(val mealName: String, val count: Int)
}

sealed class CreateOrderUseCaseError {
    data class InvalidCount(val message: String) : CreateOrderUseCaseError()
    data class InvalidMealName(val message: String) : CreateOrderUseCaseError()
    object EmptyOrder : CreateOrderUseCaseError()
}

fun NegativeValueError.toError() = CreateOrderUseCaseError.InvalidCount("Negative value")
fun EmptyMealNameError.toError() = CreateOrderUseCaseError.InvalidMealName("Meal name is empty")
fun EmptyOrder.toError() = CreateOrderUseCaseError.EmptyOrder