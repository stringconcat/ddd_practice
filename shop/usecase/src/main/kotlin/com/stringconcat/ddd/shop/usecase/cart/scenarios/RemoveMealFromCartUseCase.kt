package com.stringconcat.ddd.shop.usecase.cart.scenarios

import arrow.core.Either
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.usecase.cart.RemoveMealFromCart
import com.stringconcat.ddd.shop.usecase.cart.RemoveMealFromCartUseCaseError

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
