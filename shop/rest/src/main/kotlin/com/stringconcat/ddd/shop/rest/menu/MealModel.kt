package com.stringconcat.ddd.shop.rest.menu

import com.stringconcat.ddd.shop.usecase.menu.dto.MealInfo
import java.math.BigDecimal

data class MealModel(
    val id: Long,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val version: Long,
) {

    companion object {
        fun from(mealInfo: MealInfo): MealModel =
                MealModel(id = mealInfo.id.toLongValue(),
                        name = mealInfo.name.toStringValue(),
                        price = mealInfo.price.toBigDecimalValue(),
                        description = mealInfo.description.toStringValue(),
                        version = mealInfo.version.toLongValue())
    }
}