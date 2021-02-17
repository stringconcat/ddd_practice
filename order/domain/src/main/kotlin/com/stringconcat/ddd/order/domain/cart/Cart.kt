package com.stringconcat.ddd.order.domain.cart

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.common.types.base.AggregateRoot
import com.stringconcat.ddd.common.types.base.DomainEvent
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.order.domain.menu.Meal
import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.domain.rules.CustomerHasActiveOrderRule
import java.time.OffsetDateTime

class Cart internal constructor(
    id: CartId,
    val customerId: CustomerId,
    val created: OffsetDateTime,
    meals: Map<MealId, Count>,
    version: Version
) : AggregateRoot<CartId>(id, version) {

    internal fun addCartEvent(event: DomainEvent) {
        super.addEvent(event)
    }

    private val meals = HashMap<MealId, Count>(meals)

    fun meals() = HashMap(meals)

    fun addMeal(
        meal: Meal,
        activeOrder: CustomerHasActiveOrderRule
    ): Either<AddMealToCartError, Unit> {

        if (activeOrder.hasActiveOrder(customerId)) {
            return AddMealToCartError.HasActiveOrder.left()
        }

        val mealId = meal.id
        val count = meals[mealId]

        return if (count == null) {
            createNewMeal(mealId)
        } else {
            updateExistingMeal(mealId, count)
        }
    }

    private fun updateExistingMeal(mealId: MealId, count: Count): Either<AddMealToCartError, Unit> =
        count.increment()
            .map {
                meals[mealId] = it
                addEvent(MealHasBeenAddedToCart(id, mealId))
            }.mapLeft {
                AddMealToCartError.LimitReached
            }

    private fun createNewMeal(mealId: MealId): Either<AddMealToCartError, Unit> {
        meals[mealId] = Count.one()
        addEvent(MealHasBeenAddedToCart(id, mealId))
        return Unit.right()
    }

    fun removeMeals(mealId: MealId) {
        meals.remove(mealId)?.let {
            addEvent(MealHasBeenRemovedFromCart(id, mealId))
        }
    }
}

sealed class AddMealToCartError {
    object HasActiveOrder : AddMealToCartError()
    object LimitReached : AddMealToCartError()
}