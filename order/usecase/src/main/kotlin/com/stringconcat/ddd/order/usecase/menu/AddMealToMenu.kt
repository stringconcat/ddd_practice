package com.stringconcat.ddd.order.usecase.menu

import arrow.core.Either
import com.stringconcat.ddd.order.domain.menu.MealId

interface AddMealToMenu {
    fun execute(request: AddMealToMenuRequest): Either<AddMealToMenuUseCaseError, MealId>
}

sealed class AddMealToMenuUseCaseError(open val message: String) {
    data class InvalidName(override val message: String) : AddMealToMenuUseCaseError(message)
    data class InvalidDescription(override val message: String) : AddMealToMenuUseCaseError(message)
    data class InvalidPrice(override val message: String) : AddMealToMenuUseCaseError(message)
    object AlreadyExists : AddMealToMenuUseCaseError("Meal already exists")
}