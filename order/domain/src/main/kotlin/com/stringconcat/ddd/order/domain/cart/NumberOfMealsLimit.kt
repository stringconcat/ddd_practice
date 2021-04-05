package com.stringconcat.ddd.order.domain.cart

import com.stringconcat.ddd.common.types.common.Count

interface NumberOfMealsLimit {
    fun maximumNumberOfMeals(): Count
}
