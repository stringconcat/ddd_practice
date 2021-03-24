package com.stringconcat.ddd.shop.usecase.menu

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.order.domain.menu.MealId

class RemoveMealFromMenuUseCase(
    private val mealExtractor: MealExtractor,
    private val mealPersister: MealPersister
) : RemoveMealFromMenu {

    override fun execute(id: MealId): Either<RemoveMealFromMenuUseCaseError, Unit> {
        val meal = mealExtractor.getById(id)
        return if (meal != null) {
            meal.removeMealFromMenu()
            mealPersister.save(meal).right()
        } else {
            RemoveMealFromMenuUseCaseError.MealNotFound.left()
        }
    }
}
