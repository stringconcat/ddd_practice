package com.stringconcat.ddd.shop.usecase.menu.dto

import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.shop.domain.menu.MealDescription
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.MealName
import com.stringconcat.ddd.shop.domain.menu.Price

// На данный момент эта dto используется в нескольких сценариях.
// Тут следует быть осторожным и вовремя заметить, когда разным сценариям нужен будет разный набор данных
// Если такое произойдет - небоходимо выделить отдельный класс. В нашем случае можно оставить и так
data class MealInfo(
    val id: MealId,
    val name: MealName,
    val description: MealDescription,
    val price: Price,
    val version: Version
)