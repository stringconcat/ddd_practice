package com.stringconcat.ddd.order.usecase.cart

import arrow.core.Either

interface RemoveMealFromCart {
    fun execute(forCustomer: String, mealId: Long): Either<RemoveMealFromCartUseCaseError, Unit>
}

sealed class RemoveMealFromCartUseCaseError(val message: String) {
    object CartNotFound : RemoveMealFromCartUseCaseError("Cart not found")
}