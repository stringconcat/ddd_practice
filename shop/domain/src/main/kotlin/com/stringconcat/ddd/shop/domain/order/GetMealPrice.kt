package com.stringconcat.ddd.shop.domain.order

import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.Price

fun interface GetMealPrice {
    operator fun invoke(forMealId: MealId): Price
}