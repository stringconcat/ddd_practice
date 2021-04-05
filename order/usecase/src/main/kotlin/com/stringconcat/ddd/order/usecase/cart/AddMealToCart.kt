package com.stringconcat.ddd.order.usecase.cart

import arrow.core.Either
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.menu.MealId

interface AddMealToCart {
    fun execute(forCustomer: CustomerId, mealId: MealId): Either<AddMealToCartUseCaseError, Unit>
}

sealed class AddMealToCartUseCaseError(val message: String) {
    object MealNotFound : AddMealToCartUseCaseError("Meal not found")
    object NumberOfMealsLimitExceeded : AddMealToCartUseCaseError("Number of meals limit exceeded")
}