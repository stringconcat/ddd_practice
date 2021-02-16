package com.stringconcat.ddd.common.types.common

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.common.types.base.ValueObject

data class Count(val value: Int) : ValueObject {

    companion object {
        fun from(count: Int): Either<CreateCountError, Count> {
            return if (count < 0) {
                CreateCountError.NegativeValue.left()
            } else {
                Count(count).right()
            }
        }

        fun one(): Count {
            return Count(1)
        }
    }

    fun increment(): Either<IncrementError, Count> {
        val res = value + 1
        return if (res > value) {
            Count(res).right()
        } else {
            IncrementError.MaxValueReached.left()
        }
    }

    fun decrement(): Either<DecrementError, Count> {
        val res = value - 1
        return if (res >= 0) {
            Count(res).right()
        } else {
            DecrementError.MinValueReached.left()
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
