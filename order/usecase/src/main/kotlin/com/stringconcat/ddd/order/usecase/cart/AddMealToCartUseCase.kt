package com.stringconcat.ddd.order.usecase.cart

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.order.domain.cart.Cart
import com.stringconcat.ddd.order.domain.cart.CartIdGenerator
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.cart.NumberOfMealsExceedsLimit
import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.usecase.menu.MealExtractor

class AddMealToCartUseCase(
    private val cartExtractor: CartExtractor,
    private val idGenerator: CartIdGenerator,
    private val mealExtractor: MealExtractor,
    private val cartPersister: CartPersister,
    private val numberOfMealsExceedsLimit: NumberOfMealsExceedsLimit
) : AddMealToCart {
    override fun execute(
        forCustomer: CustomerId,
        mealId: MealId
    ): Either<AddMealToCartUseCaseError, Unit> =
        mealExtractor
            .getById(mealId)
            .rightIfNotNull { AddMealToCartUseCaseError.MealNotFound }
            .flatMap { meal ->
                val cart = getOrCreateCart(forCustomer)
                cart.addMeal(meal, numberOfMealsExceedsLimit)
                    .map { cart }
                    .mapLeft { AddMealToCartUseCaseError.NumberOfMealsLimitExceeded }
            }
            .map { cart -> cartPersister.save(cart) }

    private fun getOrCreateCart(forCustomer: CustomerId): Cart {
        return cartExtractor.getCart(forCustomer)
            ?: Cart.create(idGenerator = idGenerator, forCustomer = forCustomer)
    }
}
