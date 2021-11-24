package com.stringconcat.ddd.shop.domain.menu

import com.stringconcat.ddd.common.types.count
import com.stringconcat.ddd.shop.domain.price
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import java.math.BigDecimal
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class PriceTest {

    @ParameterizedTest
    @ValueSource(ints = [0, 1])
    fun `create price - success`(value: Int) {
        val input = BigDecimal(value)
        val result = Price.from(input)

        val price = result.shouldBeRight()
        price.toBigDecimalValue() shouldBe input.setScale(2)
    }

    @Test
    fun `create price - change scale`() {
        val value = BigDecimal("1.4")
        val result = Price.from(value)

        val price = result.shouldBeRight()
        price.toBigDecimalValue() shouldBe BigDecimal("1.40")
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
        result.toBigDecimalValue() shouldBe BigDecimal("2.89")
    }

    @Test
    fun `multiple to count`() {
        val price = price(BigDecimal("1.5"))
        val count = count(3)
        val result = price.multiple(count)
        result.toBigDecimalValue() shouldBe BigDecimal("4.50")
    }

    @Test
    fun `format as string`() {
        val priceStr = "111111122222222222"
        val price = price(BigDecimal(priceStr))
        price.toStringValue() shouldBe "$priceStr.00"
    }
}