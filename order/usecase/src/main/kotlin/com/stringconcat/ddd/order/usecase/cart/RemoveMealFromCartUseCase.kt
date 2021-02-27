package com.stringconcat.ddd.order.usecase.cart

import arrow.core.Either
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.menu.MealId

class RemoveMealFromCartUseCase(
    private val cartExtractor: CartExtractor,
    private val cartPersister: CartPersister
) : RemoveMealFromCart {

    override fun execute(forCustomer: String, mealId: Long): Either<RemoveMealFromCartUseCaseError, Unit> {
        return cartExtractor
            .getCart(forCustomer = CustomerId(forCustomer))
            .rightIfNotNull { RemoveMealFromCartUseCaseError.CartNotFound }
            .map {
                it.removeMeals(MealId(mealId))
                cartPersister.save(it)
            }
    }
}
