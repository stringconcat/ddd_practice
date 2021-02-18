package com.stringconcat.ddd.order.usecase.cart

import arrow.core.Either
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.order.domain.cart.CartFactory
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.usecase.menu.MealExtractor

class AddMealToCartUseCase(
    private val mealExtractor: MealExtractor,
    private val cartFactory: CartFactory,
    private val cartPersister: CartPersister
) {
    fun addMealToCart(customerId: String, mealId: Long): Either<AddMealToCartUseCaseError, Unit> {
        return mealExtractor.getById(MealId(mealId)).rightIfNotNull {
            AddMealToCartUseCaseError.MealNotFound
        }.map {
            val cart = cartFactory.createOrGetCart(CustomerId(customerId))
            cart.addMeal(it)
            cartPersister.save(cart)
        }
    }
}

sealed class AddMealToCartUseCaseError {
    object MealNotFound : AddMealToCartUseCaseError()
}