package com.stringconcat.ddd.order.domain.menu

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.common.types.base.ValueObject

data class MealName internal constructor(val value: String) : ValueObject {

    companion object {

        fun from(name: String): Either<CreateNameError, MealName> {
            return if (name.isNotBlank()) {
                MealName(name).right()
            } else {
                CreateNameError.EmptyString.left()
            }
        }
    }
}

sealed class CreateNameError {
    object EmptyString : CreateNameError()
}
