package com.stringconcat.ddd.shop.domain.menu

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.common.types.base.ValueObject
import com.stringconcat.ddd.common.types.error.BusinessError

data class MealName internal constructor(val value: String) : ValueObject {

    companion object {

        fun from(name: String): Either<CreateMealNameError, MealName> =
            if (name.isNotBlank()) {
                MealName(name).right()
            } else {
                CreateMealNameError.EmptyMealNameError.left()
            }
    }
}

sealed class CreateMealNameError : BusinessError {
    object EmptyMealNameError : CreateMealNameError()
}