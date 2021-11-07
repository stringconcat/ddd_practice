package com.stringconcat.ddd.kitchen.usecase.order.scenarios

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.core.zip
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.common.types.common.NegativeValueError
import com.stringconcat.ddd.kitchen.domain.order.EmptyMealNameError
import com.stringconcat.ddd.kitchen.domain.order.EmptyOrder
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrder
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderId
import com.stringconcat.ddd.kitchen.domain.order.Meal
import com.stringconcat.ddd.kitchen.domain.order.OrderItem
import com.stringconcat.ddd.kitchen.usecase.order.CreateOrder
import com.stringconcat.ddd.kitchen.usecase.order.CreateOrderRequest
import com.stringconcat.ddd.kitchen.usecase.order.CreateOrderUseCaseError

class CreateOrderHandler(
    private val extractor: KitchenOrderExtractor,
    private val persister: KitchenOrderPersister

) : CreateOrder {

    override fun execute(request: CreateOrderRequest): Either<CreateOrderUseCaseError, Unit> {
        val order = extractor.getById(KitchenOrderId(request.id)) // выпоняем дедупликацю
        return if (order != null) {
            Unit.right()
        } else {
            createNewOrder(request)
        }
    }

    private fun createNewOrder(request: CreateOrderRequest): Either<CreateOrderUseCaseError, Unit> {

        val items = request.items.map {
            transform(it.count).zip(
                transform(it.mealName)
            ) { count, mealName ->  OrderItem(mealName, count) }
        }.map {
            it.mapLeft { e -> return@createNewOrder e.left() }
        }.mapNotNull { it.orNull() }

        return KitchenOrder.create(id = KitchenOrderId(request.id), orderItems = items)
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

fun NegativeValueError.toError() = CreateOrderUseCaseError.InvalidCount("Negative value")
fun EmptyMealNameError.toError() = CreateOrderUseCaseError.InvalidMealName("Meal name is empty")
fun EmptyOrder.toError() = CreateOrderUseCaseError.EmptyOrder