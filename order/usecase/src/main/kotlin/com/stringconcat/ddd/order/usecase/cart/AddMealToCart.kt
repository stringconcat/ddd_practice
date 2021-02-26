package com.stringconcat.ddd.order.usecase.cart

import arrow.core.Either

interface AddMealToCart {
    fun execute(forCustomer: String, mealId: Long): Either<AddMealToCartUseCaseError, Unit>
}

sealed class AddMealToCartUseCaseError {
    object MealNotFound : AddMealToCartUseCaseError()
}