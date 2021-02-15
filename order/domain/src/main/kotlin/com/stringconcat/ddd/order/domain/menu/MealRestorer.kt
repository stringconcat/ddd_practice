package com.stringconcat.ddd.order.domain.menu

import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Address

object MealRestorer {

    fun restoreMeal(
        id: MealId,
        name: MealName,
        removed: Boolean,
        description: MealDescription,
        address: Address,
        price: Price,
        version: Version
    ): Meal {
        return Meal(
            id = id,
            name = name,
            description = description,
            address = address,
            price = price,
            version = version
        ).apply { this.removed = removed }
    }
}