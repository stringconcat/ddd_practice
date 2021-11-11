package com.stringconcat.ddd.shop.usecase.menu.scenarios

import arrow.core.Either
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.usecase.menu.GetMealById
import com.stringconcat.ddd.shop.usecase.menu.GetMealByIdUseCaseError
import com.stringconcat.ddd.shop.usecase.menu.access.MealExtractor
import com.stringconcat.ddd.shop.usecase.menu.dto.MealInfo

class GetMealByIdUseCase(private val mealExtractor: MealExtractor) : GetMealById {

    override fun execute(id: MealId): Either<GetMealByIdUseCaseError, MealInfo> {
            return mealExtractor.getById(id)
                .takeIf { it != null && it.visible() }
                .rightIfNotNull { GetMealByIdUseCaseError.MealNotFound }
                .map { meal ->
                    MealInfo(id = meal.id,
                    name = meal.name,
                    description = meal.description,
                    price = meal.price,
                    version = meal.version)
                }
    }
}