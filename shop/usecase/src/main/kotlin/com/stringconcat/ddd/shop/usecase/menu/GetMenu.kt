package com.stringconcat.ddd.shop.usecase.menu

import com.stringconcat.ddd.shop.domain.menu.MealDescription
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.MealName
import com.stringconcat.ddd.shop.domain.menu.Price

interface GetMenu {
    fun execute(): List<MealInfo>
}

data class MealInfo(
    val id: MealId,
    val name: MealName,
    val description: MealDescription,
    val price: Price
)