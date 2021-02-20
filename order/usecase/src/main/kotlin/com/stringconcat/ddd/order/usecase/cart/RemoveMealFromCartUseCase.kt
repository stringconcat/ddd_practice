package com.stringconcat.ddd.order.usecase.cart

import arrow.core.Either
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.menu.MealId

class RemoveMealFromCartUseCase(
    private val cartExtractor: CartExtractor,
    private val cartPersister: CartPersister
) {

    fun removeMealFromCart(customerId: String, mealId: Long): Either<RemoveMealFromCartCaseError, Unit> {
        return cartExtractor
            .getCart(forCustomer = CustomerId(customerId))
            .rightIfNotNull { RemoveMealFromCartCaseError.CartNotFound }
            .map {
                it.removeMeals(MealId(mealId))
                cartPersister.save(it)
            }
    }
}

sealed class RemoveMealFromCartCaseError {
    object CartNotFound : RemoveMealFromCartCaseError()
}