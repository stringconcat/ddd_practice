package com.stringconcat.ddd.order.usecase.menu

import com.stringconcat.ddd.order.domain.menu.MealDescription
import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.domain.menu.MealName
import com.stringconcat.ddd.order.domain.menu.Price

interface GetMenu {
    fun execute(): List<MealInfo>
}

data class MealInfo(
    val id: MealId,
    val name: MealName,
    val description: MealDescription,
    val price: Price
)