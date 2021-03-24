package com.stringconcat.ddd.shop.usecase.cart

import arrow.core.Either
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.menu.MealId

interface AddMealToCart {
    fun execute(forCustomer: CustomerId, mealId: MealId): Either<AddMealToCartUseCaseError, Unit>
}

sealed class AddMealToCartUseCaseError(val message: String) {
    object MealNotFound : AddMealToCartUseCaseError("Meal not found")
}