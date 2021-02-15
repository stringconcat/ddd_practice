package com.stringconcat.ddd.order.domain.menu

import arrow.core.Either
import com.stringconcat.ddd.common.types.base.ValueObject

data class MealDescription internal constructor(val value: String) : ValueObject {

    companion object {

        fun from(description: String): Either<CreateDescriptionError, MealDescription> {
            return if (description.isNotBlank()) {
                Either.right(MealDescription(description))
            } else {
                Either.left(CreateDescriptionError.EmptyString)
            }
        }
    }
}

sealed class CreateDescriptionError {
    object EmptyString : CreateDescriptionError()
}
