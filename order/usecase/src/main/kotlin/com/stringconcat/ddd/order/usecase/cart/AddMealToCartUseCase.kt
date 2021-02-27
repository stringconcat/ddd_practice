package com.stringconcat.ddd.order.usecase.cart

import arrow.core.Either
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.order.domain.cart.Cart
import com.stringconcat.ddd.order.domain.cart.CartIdGenerator
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.usecase.menu.MealExtractor

class AddMealToCartUseCase(
    private val cartExtractor: CartExtractor,
    private val idGenerator: CartIdGenerator,
    private val mealExtractor: MealExtractor,
    private val cartPersister: CartPersister
) : AddMealToCart {
    override fun execute(forCustomer: CustomerId, mealId: MealId): Either<AddMealToCartUseCaseError, Unit> {
        return mealExtractor.getById(mealId).rightIfNotNull {
            AddMealToCartUseCaseError.MealNotFound
        }.map {
            val cart = getOrCreateCart(forCustomer)
            cart.addMeal(it)
            cartPersister.save(cart)
        }
    }

    private fun getOrCreateCart(customerId: CustomerId): Cart {
        return cartExtractor.getCart(customerId)
            ?: Cart.create(idGenerator = idGenerator, forCustomer = customerId)
    }
}
