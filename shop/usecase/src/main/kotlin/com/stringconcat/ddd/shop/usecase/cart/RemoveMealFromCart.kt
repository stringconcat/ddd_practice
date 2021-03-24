package com.stringconcat.ddd.shop.usecase.cart

import arrow.core.Either
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.menu.MealId

interface RemoveMealFromCart {
    fun execute(forCustomer: CustomerId, mealId: MealId): Either<RemoveMealFromCartUseCaseError, Unit>
}

sealed class RemoveMealFromCartUseCaseError(val message: String) {
    object CartNotFound : RemoveMealFromCartUseCaseError("Cart not found")
}