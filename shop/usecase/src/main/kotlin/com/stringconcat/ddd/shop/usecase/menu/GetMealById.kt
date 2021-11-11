package com.stringconcat.ddd.shop.usecase.menu

import arrow.core.Either
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.usecase.menu.dto.MealInfo

interface GetMealById {
    fun execute(id: MealId): Either<GetMealByIdUseCaseError, MealInfo>
}

sealed class GetMealByIdUseCaseError {
    object MealNotFound : GetMealByIdUseCaseError()
}