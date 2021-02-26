package com.stringconcat.ddd.order.usecase.cart

import arrow.core.Either
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.usecase.menu.MealExtractor

class GetCartUseCase(
    private val mealExtractor: MealExtractor,
    private val cartExtractor: CartExtractor
) : GetCart {
    override fun execute(forCustomer: String): Either<GetCartUseCaseError, CartInfo> {
        val cart = cartExtractor.getCart(CustomerId(forCustomer))
        return cart.rightIfNotNull { GetCartUseCaseError.CartNotFound }
            .map { c ->
                c.meals().map {
                    val meal = checkNotNull(mealExtractor.getById(it.key)) {
                        "Meal #${it.key} not found"
                    }
                    CartItem(
                        mealId = it.key.value,
                        mealName = meal.name.value,
                        count = it.value.value
                    )
                }
            }.map {
                CartInfo(customerId = forCustomer, items = it)
            }
    }
}