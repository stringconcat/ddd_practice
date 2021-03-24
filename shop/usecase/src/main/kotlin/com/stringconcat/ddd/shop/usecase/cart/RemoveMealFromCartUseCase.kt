package com.stringconcat.ddd.shop.usecase.cart

import arrow.core.Either
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.menu.MealId

class RemoveMealFromCartUseCase(
    private val cartExtractor: CartExtractor,
    private val cartPersister: CartPersister
) : RemoveMealFromCart {

    override fun execute(forCustomer: CustomerId, mealId: MealId): Either<RemoveMealFromCartUseCaseError, Unit> {
        return cartExtractor
            .getCart(forCustomer = forCustomer)
            .rightIfNotNull { RemoveMealFromCartUseCaseError.CartNotFound }
            .map {
                it.removeMeals(mealId)
                cartPersister.save(it)
            }
    }
}
