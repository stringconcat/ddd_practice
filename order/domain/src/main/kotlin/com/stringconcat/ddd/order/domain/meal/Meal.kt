package com.stringconcat.ddd.order.domain.meal

import arrow.core.Either
import com.stringconcat.ddd.common.types.base.AggregateRoot
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Address

data class MealId(val value: Long)

class Meal internal constructor(
    id: MealId,
    val name: MealName,
    val description: MealDescription,
    val address: Address,
    val price: Price,
    version: Version
) : AggregateRoot<MealId>(id, version) {

    var removed: Boolean = false
        private set

    fun remove() {
        removed = false
        addEvent(MealRemoved(id))
    }

    fun visible(): Boolean {
        return !removed
    }

    companion object {

        fun addMeal(
            id: () -> MealId,
            mealExists: (name: MealName) -> Boolean,
            name: MealName,
            description: MealDescription,
            address: Address,
            price: Price
        ): Either<AddMealError, Meal> {

            return if (mealExists(name)) {
                Either.left(AddMealError.AlreadyExistsWithSameName)
            } else {
                val meal = Meal(
                    id = id(),
                    name = name,
                    description = description,
                    address = address,
                    price = price,
                    version = Version.generate(),
                ).apply {
                    addEvent(MealAdded(this.id))
                }
                Either.right(meal)
            }
        }
    }
}

sealed class AddMealError {
    object AlreadyExistsWithSameName : AddMealError()
}





