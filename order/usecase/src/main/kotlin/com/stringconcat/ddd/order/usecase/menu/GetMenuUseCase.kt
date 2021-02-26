package com.stringconcat.ddd.order.usecase.menu

import java.math.BigDecimal

class GetMenuUseCase(private val mealExtractor: MealExtractor) : GetMenu {

    override fun execute(): List<MealInfo> {
        return mealExtractor.getAll()
            .map {
                MealInfo(
                    id = it.id.value,
                    name = it.name.value,
                    description = it.description.value,
                    price = it.price.value
                )
            }
    }
}

data class MealInfo(
    val id: Long,
    val name: String,
    val description: String,
    val price: BigDecimal
)