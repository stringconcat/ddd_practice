package com.stringconcat.ddd.order.usecase.menu

import arrow.core.Either
import com.stringconcat.ddd.order.domain.menu.MealId

interface RemoveMealFromMenu {
    fun execute(id: MealId): Either<RemoveMealFromMenuUseCaseError, Unit>
}

sealed class RemoveMealFromMenuUseCaseError(val message: String) {
    object MealNotFound : RemoveMealFromMenuUseCaseError("Meal not found")
}