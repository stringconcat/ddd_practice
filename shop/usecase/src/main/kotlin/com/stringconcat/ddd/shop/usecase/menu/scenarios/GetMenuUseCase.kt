package com.stringconcat.ddd.shop.usecase.menu.scenarios

import com.stringconcat.ddd.shop.usecase.menu.GetMenu
import com.stringconcat.ddd.shop.usecase.menu.access.MealExtractor
import com.stringconcat.ddd.shop.usecase.menu.dto.MealInfo

class GetMenuUseCase(private val mealExtractor: MealExtractor) : GetMenu {

    override fun execute(): List<MealInfo> {
        return mealExtractor.getAll()
            .map {
                MealInfo(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    price = it.price,
                    version = it.version
                )
            }
    }
}