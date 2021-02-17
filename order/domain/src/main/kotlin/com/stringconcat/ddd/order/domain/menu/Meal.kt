package com.stringconcat.ddd.order.domain.menu

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.common.types.base.AggregateRoot
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.error.BusinessError
import com.stringconcat.ddd.order.domain.rules.MealAlreadyExistsRule

class Meal internal constructor(
    id: MealId,
    val name: MealName,
    val description: MealDescription,
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
            idGenerator: MealIdGenerator,
            mealExistsRule: MealAlreadyExistsRule,
            name: MealName,
            description: MealDescription,
            price: Price
        ): Either<AlreadyExistsWithSameNameError, Meal> =
            if (mealExistsRule.exists(name)) {
                AlreadyExistsWithSameNameError.left()
            } else {
                val meal = Meal(
                    id = idGenerator.generateId(),
                    name = name,
                    description = description,
                    price = price,
                    version = Version.generate(),
                ).apply {
                    addEvent(MealHasBeenAddedToMenu(this.id))
                }
                meal.right()
            }
    }
}

object AlreadyExistsWithSameNameError: BusinessError