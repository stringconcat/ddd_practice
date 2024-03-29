package com.stringconcat.ddd.shop.usecase.cart.scenarios

import arrow.core.Either
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.shop.domain.cart.Cart
import com.stringconcat.ddd.shop.domain.cart.CartIdGenerator
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.usecase.cart.AddMealToCart
import com.stringconcat.ddd.shop.usecase.cart.AddMealToCartUseCaseError
import com.stringconcat.ddd.shop.usecase.cart.access.CartExtractor
import com.stringconcat.ddd.shop.usecase.cart.access.CartPersister
import com.stringconcat.ddd.shop.usecase.menu.access.MealExtractor

class AddMealToCartUseCase(
    private val cartExtractor: CartExtractor,
    private val idGenerator: CartIdGenerator,
    private val mealExtractor: MealExtractor,
    private val cartPersister: CartPersister
) : AddMealToCart {
    override fun execute(
        forCustomer: CustomerId,
        mealId: MealId
    ): Either<AddMealToCartUseCaseError, Unit> =
        mealExtractor
            .getById(mealId)
            .rightIfNotNull { AddMealToCartUseCaseError.MealNotFound }
            .map { meal -> getOrCreateCart(forCustomer).apply { addMeal(meal) } }
            .map { cart -> cartPersister.save(cart) }

    private fun getOrCreateCart(forCustomer: CustomerId): Cart {
        return cartExtractor.getCart(forCustomer)
            ?: Cart.create(idGenerator = idGenerator, forCustomer = forCustomer)
    }
}
