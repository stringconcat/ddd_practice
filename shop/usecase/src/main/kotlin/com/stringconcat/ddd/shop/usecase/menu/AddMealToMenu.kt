package com.stringconcat.ddd.shop.usecase.menu

import arrow.core.Either
import com.stringconcat.ddd.order.domain.menu.MealId

interface AddMealToMenu {
    fun execute(request: AddMealToMenuRequest): Either<AddMealToMenuUseCaseError, MealId>
}

sealed class AddMealToMenuUseCaseError(open val message: String) {
    object AlreadyExists : AddMealToMenuUseCaseError("Meal already exists")
}