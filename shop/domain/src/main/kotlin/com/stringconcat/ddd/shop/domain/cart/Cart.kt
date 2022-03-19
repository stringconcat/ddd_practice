package com.stringconcat.ddd.shop.domain.cart

import com.stringconcat.ddd.common.types.base.AggregateRoot
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.shop.domain.menu.Meal
import com.stringconcat.ddd.shop.domain.menu.MealId
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

    fun addMeal(meal: Meal) {
        val mealId = meal.id
        val countOfCurrentlyMealsInCart = meals[mealId]

        return if (countOfCurrentlyMealsInCart == null) {
            createNewMeal(mealId)
        } else {
            updateExistingMeal(mealId, countOfCurrentlyMealsInCart)
        }
    }

    private fun createNewMeal(
        mealId: MealId
    ) {
        meals[mealId] = Count.one()
        addEvent(MealAddedToCartDomainEvent(id, mealId))
    }

    private fun updateExistingMeal(
        mealId: MealId,
        count: Count
    ) {
        count.increment()
            .map { incrementedCount ->
                meals[mealId] = incrementedCount
                addEvent(MealAddedToCartDomainEvent(id, mealId))
            }.mapLeft {
                error("You have too much the same meals in you cart") // в примере не будем это обрабатывать
            }
    }

    fun removeMeals(mealId: MealId) {
        meals.remove(mealId)?.let {
            addEvent(MealRemovedFromCartDomainEvent(id, mealId))
        }
    }
}