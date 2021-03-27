package com.stringconcat.ddd.order.usecase.rules

import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.order.domain.cart.Cart
import com.stringconcat.ddd.order.domain.cart.NumberOfMealsExceedsLimit

class NumberOfMealsExceedsLimitImpl(
    private val maximumNumberOfMeals: Count
) : NumberOfMealsExceedsLimit {

    override fun check(cart: Cart): Boolean {
        val numberOfMeals = cart.meals().asSequence()
            .sumBy { it.value.value }
        return maximumNumberOfMeals.value < numberOfMeals
    }
}
