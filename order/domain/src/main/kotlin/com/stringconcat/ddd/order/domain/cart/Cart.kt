package com.stringconcat.ddd.order.domain.cart

import com.stringconcat.ddd.common.types.base.AggregateRoot
import com.stringconcat.ddd.common.types.base.DomainEvent
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.order.domain.menu.Meal
import com.stringconcat.ddd.order.domain.menu.MealId
import java.time.OffsetDateTime

class Cart internal constructor(
    id: CartId,
    val customerId: CustomerId,
    val created: OffsetDateTime,
    meals: Map<MealId, Count>,
    version: Version
) : AggregateRoot<CartId>(id, version) {

    private val meals = HashMap<MealId, Count>(meals)

    internal fun addCartEvent(event: DomainEvent) {
        super.addEvent(event)
    }

    fun meals(): Map<MealId, Count> = HashMap(meals)

    fun addMeal(
        meal: Meal
    ) {
        val mealId = meal.id
        val count = meals[mealId]

        return if (count == null) {
            createNewMeal(mealId)
        } else {
            updateExistingMeal(mealId, count)
        }
    }

    private fun updateExistingMeal(
        mealId: MealId,
        count: Count
    ) {
        count.increment()
            .map {
                meals[mealId] = it
                addEvent(MealHasBeenAddedToCart(id, mealId))
            }.mapLeft {
                error("Limit reached") // в примере не будем это обрабатывать
            }
    }

    private fun createNewMeal(
        mealId: MealId
    ) {
        meals[mealId] = Count.one()
        addEvent(MealHasBeenAddedToCart(id, mealId))
    }

    fun removeMeals(mealId: MealId) {
        meals.remove(mealId)?.let {
            addEvent(MealHasBeenRemovedFromCart(id, mealId))
        }
    }
}