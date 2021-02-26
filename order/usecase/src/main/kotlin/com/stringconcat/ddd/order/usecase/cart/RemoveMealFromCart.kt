package com.stringconcat.ddd.order.usecase.cart

import arrow.core.Either

interface RemoveMealFromCart {
    fun execute(customerId: String, mealId: Long): Either<RemoveMealFromCartCaseError, Unit>
}

sealed class RemoveMealFromCartCaseError {
    object CartNotFound : RemoveMealFromCartCaseError()
}