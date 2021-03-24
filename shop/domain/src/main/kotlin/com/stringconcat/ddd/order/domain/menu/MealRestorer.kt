package com.stringconcat.ddd.order.domain.menu

import com.stringconcat.ddd.common.types.base.Version

object MealRestorer {

    fun restoreMeal(
        id: MealId,
        name: MealName,
        removed: Boolean,
        description: MealDescription,
        price: Price,
        version: Version
    ): Meal {
        return Meal(
            id = id,
            name = name,
            description = description,
            price = price,
            version = version
        ).apply { this.removed = removed }
    }
}