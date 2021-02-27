package com.stringconcat.ddd.order.usecase.menu

import arrow.core.Either

interface RemoveMealFromMenu {
    fun execute(id: Long): Either<RemoveMealFromMenuUseCaseError, Unit>
}

sealed class RemoveMealFromMenuUseCaseError(val message: String) {
    object MealNotFound : RemoveMealFromMenuUseCaseError("Meal not found")
}