package com.stringconcat.ddd.order.domain.menu

import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigDecimal

internal class PriceTest {

    @Test
    fun `create price - success`() {
        val price = BigDecimal("1.40")
        val result = Price.from(price)

        result shouldBeRight {
            it.value shouldBe price
        }
    }

    @Test
    fun `create price - change scale`() {
        val price = BigDecimal("1.4")
        val result = Price.from(price)

        result shouldBeRight {
            it.value shouldBe BigDecimal("1.40")
        }
    }

    @Test
    fun `create price - invalid scale`() {
        val price = BigDecimal("1.411")
        val result = Price.from(price)

        result shouldBeLeft CreatePriceError.InvalidScale
    }

    @ParameterizedTest
    @ValueSource(ints = [0, -1, -10000])
    fun `create price - non positive value`(value: Int) {

        val price = BigDecimal(value)
        val result = Price.from(price)

        result shouldBeLeft CreatePriceError.NonPositiveValue
    }
}