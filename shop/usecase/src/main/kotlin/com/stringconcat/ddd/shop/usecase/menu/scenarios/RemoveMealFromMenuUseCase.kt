package com.stringconcat.ddd.shop.usecase.menu.scenarios

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.usecase.menu.RemoveMealFromMenu
import com.stringconcat.ddd.shop.usecase.menu.RemoveMealFromMenuUseCaseError
import com.stringconcat.ddd.shop.usecase.menu.access.MealExtractor
import com.stringconcat.ddd.shop.usecase.menu.access.MealPersister

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
