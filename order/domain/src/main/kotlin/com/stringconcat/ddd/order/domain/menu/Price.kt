package com.stringconcat.ddd.order.domain.menu

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.common.types.base.ValueObject
import com.stringconcat.ddd.common.types.common.Count
import java.math.BigDecimal

data class Price internal constructor(val value: BigDecimal) : ValueObject {

    companion object {

        private const val SCALE = 2

        fun zero(): Price = Price(BigDecimal.ZERO.setScale(SCALE))

        fun from(price: BigDecimal): Either<CreatePriceError, Price> {
            return when {
                price.scale() > SCALE ->
                    CreatePriceError.InvalidScale.left()

                price < BigDecimal.ZERO ->
                    CreatePriceError.NegativeValue.left()

                else -> Price(price.setScale(SCALE)).right()
            }
        }
    }

    fun add(price: Price): Price {
        return Price(price.value.add(this.value))
    }

    fun multiple(multiplicator: Count): Price {
        val newValue = this.value.multiply(BigDecimal(multiplicator.value))
        return Price(newValue)
    }
}

sealed class CreatePriceError {
    object InvalidScale : CreatePriceError()
    object NegativeValue : CreatePriceError()
}