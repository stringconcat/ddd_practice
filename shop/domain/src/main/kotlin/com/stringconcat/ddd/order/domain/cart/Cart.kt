package com.stringconcat.ddd.order.domain.cart

import com.stringconcat.ddd.common.types.base.AggregateRoot
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.order.domain.menu.Meal
import com.stringconcat.ddd.order.domain.menu.MealId
import java.time.OffsetDateTime

class Cart internal constructor(
    id: CartId,
    val forCustomer: CustomerId,
    val created: OffsetDateTime,
    meals: Map<MealId, Count>,
    version: Version
) : AggregateRoot<CartId>(id, version) {

    companion object {

        fun create(idGenerator: CartIdGenerator, forCustomer: CustomerId): Cart {
            return Cart(
                id = idGenerator.generate(),
                forCustomer = forCustomer,
                created = OffsetDateTime.now(),
                version = Version.new(),
                meals = emptyMap()
            ).apply {
                addEvent(CartCreatedDomainEvent(cartId = this.id))
            }
        }
    }

    private val meals = HashMap<MealId, Count>(meals)

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
                addEvent(MealAddedToCartDomainEvent(id, mealId))
            }.mapLeft {
                error("Limit reached") // в примере не будем это обрабатывать
            }
    }

    private fun createNewMeal(
        mealId: MealId
    ) {
        meals[mealId] = Count.one()
        addEvent(MealAddedToCartDomainEvent(id, mealId))
    }

    fun removeMeals(mealId: MealId) {
        meals.remove(mealId)?.let {
            addEvent(MealRemovedFromCartDomainEvent(id, mealId))
        }
    }
}