package com.stringconcat.ddd.order.usecase.menu

class GetMenuUseCase(private val mealExtractor: MealExtractor) : GetMenu {

    override fun execute(): List<MealInfo> {
        return mealExtractor.getAll()
            .map {
                MealInfo(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    price = it.price
                )
            }
    }
}