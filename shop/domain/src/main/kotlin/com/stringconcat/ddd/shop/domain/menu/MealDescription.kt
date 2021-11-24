package com.stringconcat.ddd.shop.domain.menu

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.common.types.base.ValueObject
import com.stringconcat.ddd.common.types.error.BusinessError

data class MealDescription internal constructor(private val value: String) : ValueObject {

    fun toStringValue() = value

    companion object {

        fun from(description: String): Either<CreateMealDescriptionError, MealDescription> =
            if (description.isNotBlank()) {
                MealDescription(description).right()
            } else {
                CreateMealDescriptionError.EmptyDescriptionError.left()
            }
    }
}

sealed class CreateMealDescriptionError : BusinessError {
    object EmptyDescriptionError : CreateMealDescriptionError()
}
