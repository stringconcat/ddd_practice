package com.stringconcat.ddd.shop.usecase.menu

import com.stringconcat.ddd.shop.usecase.menu.dto.MealInfo

interface GetMenu {
    fun execute(): List<MealInfo>
}