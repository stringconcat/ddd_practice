package com.stringconcat.ddd.order.domain.menu

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.common.types.base.AggregateRoot
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.error.BusinessError

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
            addEvent(MealRemovedFromMenuDomainEvent(id))
        }
    }

    fun visible(): Boolean {
        return !removed
    }

    companion object {

        fun addMealToMenu(
            idGenerator: MealIdGenerator,
            mealExists: MealAlreadyExists,
            name: MealName,
            description: MealDescription,
            price: Price
        ): Either<AlreadyExistsWithSameNameError, Meal> =
            if (mealExists.check(name)) {
                AlreadyExistsWithSameNameError.left()
            } else {
                Meal(
                    id = idGenerator.generate(),
                    name = name,
                    description = description,
                    price = price,
                    version = Version.new(),
                ).apply {
                    addEvent(MealAddedToMenuDomainEvent(this.id))
                }.right()
            }
    }
}

object AlreadyExistsWithSameNameError : BusinessError