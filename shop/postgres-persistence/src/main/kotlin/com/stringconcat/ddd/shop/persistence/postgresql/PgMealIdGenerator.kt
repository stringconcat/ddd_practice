package com.stringconcat.ddd.shop.persistence.postgresql

import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.MealIdGenerator

class PgMealIdGenerator : MealIdGenerator {
    override fun generate(): MealId {
        return MealId(1)
    }
}