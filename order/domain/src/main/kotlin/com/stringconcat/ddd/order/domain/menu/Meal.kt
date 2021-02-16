package com.stringconcat.ddd.order.domain.menu

import arrow.core.Either
import arrow.core.left
import arrow.core.right
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
        internal set

    fun removeMealFromMenu() {
        if (!removed) {
            removed = true
            addEvent(MealHasBeenRemovedFromMenu(id))
        }
    }

    fun visible(): Boolean {
        return !removed
    }

    companion object {

        fun addMealToMenu(
            id: () -> MealId,
            mealExists: (name: MealName) -> Boolean,
            name: MealName,
            description: MealDescription,
            address: Address,
            price: Price
        ): Either<AddMealToMenuError, Meal> {

            return if (mealExists(name)) {
                AddMealToMenuError.AlreadyExistsWithSameName.left()
            } else {
                val meal = Meal(
                    id = id(),
                    name = name,
                    description = description,
                    address = address,
                    price = price,
                    version = Version.generate(),
                ).apply {
                    addEvent(MealHasBeenAddedToMenu(this.id))
                }
                meal.right()
            }
        }
    }
}

sealed class AddMealToMenuError {
    object AlreadyExistsWithSameName : AddMealToMenuError()
}