package com.stringconcat.ddd.order.usecase.cart

import arrow.core.Either

interface AddMealToCart {
    fun execute(forCustomer: String, mealId: Long): Either<AddMealToCartUseCaseError, Unit>
}

sealed class AddMealToCartUseCaseError(val message: String) {
    object MealNotFound : AddMealToCartUseCaseError("Meal not found")
}