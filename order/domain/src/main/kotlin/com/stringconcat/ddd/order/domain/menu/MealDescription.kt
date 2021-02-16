package com.stringconcat.ddd.order.domain.menu

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.common.types.base.ValueObject

data class MealDescription internal constructor(val value: String) : ValueObject {

    companion object {

        fun from(description: String): Either<EmptyDescriptionError, MealDescription> {
            return if (description.isNotBlank()) {
                MealDescription(description).right()
            } else {
                EmptyDescriptionError.left()
            }
        }
    }
}

object EmptyDescriptionError
