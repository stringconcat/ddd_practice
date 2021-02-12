package com.stringconcat.ddd.order.domain.meal

import arrow.core.Either
import com.stringconcat.ddd.common.types.base.ValueObject

data class Name internal constructor(val value: String) : ValueObject {

    companion object {

        fun from(name: String): Either<CreateNameError, Name> {
            return if (name.isNotBlank()) {
                Either.right(Name(name))
            } else {
                Either.left(CreateNameError.EmptyString)
            }
        }
    }
}

sealed class CreateNameError {
    object EmptyString : CreateNameError()
}
