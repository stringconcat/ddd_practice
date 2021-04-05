package com.stringconcat.ddd.order.domain.cart

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.common.types.base.AggregateRoot
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.common.types.error.BusinessError
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
        meal: Meal,
        numberOfMealsLimit: NumberOfMealsLimit
    ): Either<MealsLimitExceededError, Unit> {
        val mealsLimitExceeded = numberOfMeals() >= numberOfMealsLimit.maximumNumberOfMeals().value
        if (mealsLimitExceeded) {
            return MealsLimitExceededError.left()
        }

        val mealId = meal.id
        val count = meals[mealId]

        return if (count == null) {
            createNewMeal(mealId)
        } else {
            updateExistingMeal(mealId, count)
        }.right()
    }

    private fun numberOfMeals() =
        meals().asSequence()
            .sumBy { it.value.value }

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

object MealsLimitExceededError : BusinessError