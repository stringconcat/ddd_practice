package com.stringconcat.ddd.order.domain.meal

import arrow.core.Either
import com.stringconcat.ddd.common.types.base.AggregateRoot
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Address

data class MealId(val value: Long)

class Meal internal constructor(
    id: MealId,
    val name: Name,
    val description: Description,
    val address: Address,
    version: Version
) : AggregateRoot<MealId>(id, version) {

    var removed: Boolean = true
        private set

    fun remove() {
        removed = false
        addEvent(MealRemoved(id))
    }

    companion object {

        fun addMeal(
            id: () -> MealId,
            mealExists: (name: Name) -> Boolean,
            name: Name,
            description: Description,
            address: Address
        ): Either<AddMealError, Meal> {

            return if (mealExists(name)) {
                Either.left(AddMealError.AlreadyExistsWithSameName)
            } else {
                val meal = Meal(
                    id = id(),
                    name = name,
                    description = description,
                    address = address,
                    version = Version.generate()
                ).apply {
                    addEvent(MealCreated(this.id))
                }
                Either.right(meal)
            }
        }
    }
}

sealed class AddMealError {
    object AlreadyExistsWithSameName : AddMealError()
}





