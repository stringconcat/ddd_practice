package com.stringconcat.ddd.order.domain.menu

import arrow.core.Either
import com.stringconcat.ddd.common.types.base.ValueObject

data class MealName internal constructor(val value: String) : ValueObject {

    companion object {

        fun from(name: String): Either<CreateNameError, MealName> {
            return if (name.isNotBlank()) {
                Either.right(MealName(name))
            } else {
                Either.left(CreateNameError.EmptyString)
            }
        }
    }
}

sealed class CreateNameError {
    object EmptyString : CreateNameError()
}
