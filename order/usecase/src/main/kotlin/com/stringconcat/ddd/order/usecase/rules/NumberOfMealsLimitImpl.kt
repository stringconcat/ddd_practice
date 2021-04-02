package com.stringconcat.ddd.order.usecase.rules

import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.order.domain.cart.NumberOfMealsLimit

class NumberOfMealsLimitImpl(
    private val maximumNumberOfMealsProvider: () -> Count
) : NumberOfMealsLimit {

    override fun maximumNumberOfMeals(): Count {
        return maximumNumberOfMealsProvider()
    }
}
