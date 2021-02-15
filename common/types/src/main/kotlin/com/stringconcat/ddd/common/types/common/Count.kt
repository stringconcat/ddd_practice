package com.stringconcat.ddd.common.types.common

import arrow.core.Either
import com.stringconcat.ddd.common.types.base.ValueObject

data class Count(val value: Int) : ValueObject {

    companion object {
        fun from(count: Int): Either<CreateCountError, Count> {
            return if (count < 0) {
                Either.left(CreateCountError.NegativeValue)
            } else {
                Either.right(Count(count))
            }
        }

        fun one(): Count {
            return Count(1)
        }
    }

    fun increment(): Either<IncrementError, Count> {
        val res = value + 1
        return if (res > value) {
            Either.right(Count(res))
        } else {
            Either.left(IncrementError.MaxValueReached)
        }
    }

    fun decrement(): Either<DecrementError, Count> {
        val res = value - 1
        return if (res >= 0) {
            Either.right(Count(res))
        } else {
            Either.left(DecrementError.MinValueReached)
        }
    }

    fun isMin() = value == 0

    fun isMax() = value == Int.MAX_VALUE
}

sealed class CreateCountError {
    object NegativeValue : CreateCountError()
}

sealed class IncrementError {
    object MaxValueReached : IncrementError()
}

sealed class DecrementError {
    object MinValueReached : DecrementError()
}
