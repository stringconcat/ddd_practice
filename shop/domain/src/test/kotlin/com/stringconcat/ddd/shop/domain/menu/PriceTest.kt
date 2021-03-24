package com.stringconcat.ddd.shop.domain.menu

import com.stringconcat.ddd.shop.domain.count
import com.stringconcat.ddd.shop.domain.price
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigDecimal

internal class PriceTest {

    @ParameterizedTest
    @ValueSource(ints = [0, 1])
    fun `create price - success`(value: Int) {
        val price = BigDecimal(value)
        val result = Price.from(price)

        result shouldBeRight {
            it.value shouldBe price.setScale(2)
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

    @Test
    fun `create price - negative value`() {

        val price = BigDecimal(-1)
        val result = Price.from(price)

        result shouldBeLeft CreatePriceError.NegativeValue
    }

    @Test
    fun `add price`() {
        val price1 = price(BigDecimal("1.44"))
        val price2 = price(BigDecimal("1.45"))

        val result = price1.add(price2)
        result.value shouldBe BigDecimal("2.89")
    }

    @Test
    fun `multiple to count`() {
        val price = price(BigDecimal("1.5"))
        val count = count(3)
        val result = price.multiple(count)
        result.value shouldBe BigDecimal("4.50")
    }
}