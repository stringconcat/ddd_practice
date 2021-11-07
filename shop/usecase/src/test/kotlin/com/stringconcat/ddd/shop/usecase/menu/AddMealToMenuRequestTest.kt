package com.stringconcat.ddd.shop.usecase.menu

import com.stringconcat.ddd.shop.domain.mealDescription
import com.stringconcat.ddd.shop.domain.mealName
import com.stringconcat.ddd.shop.domain.price
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import java.math.BigDecimal
import org.junit.jupiter.api.Test

class AddMealToMenuRequestTest {

    @Test
    fun `successfully created`() {
        val name = mealName()
        val description = mealDescription()
        val price = price()

        val result = AddMealToMenuRequest.from(
            name = name.value,
            description = description.value,
            price = price.value
        )
        result shouldBeRight AddMealToMenuRequest(name, description, price)
    }

    @Test
    fun `invalid name`() {

        val name = ""
        val description = mealDescription()
        val price = price()

        val result = AddMealToMenuRequest.from(
            name = name,
            description = description.value,
            price = price.value
        )

        result shouldBeLeft InvalidMealParameters("Empty name")
    }

    @Test
    fun `invalid description`() {
        val name = mealName()
        val description = ""
        val price = price()

        val result = AddMealToMenuRequest.from(
            name = name.value,
            description = description,
            price = price.value
        )

        result shouldBeLeft InvalidMealParameters("Empty description")
    }

    @Test
    fun `invalid price - negative value`() {
        val name = mealName()
        val description = mealDescription()
        val price = BigDecimal("-1")

        val result = AddMealToMenuRequest.from(
            name = name.value,
            description = description.value,
            price = price
        )

        result shouldBeLeft InvalidMealParameters("Negative value")
    }

    @Test
    fun `invalid price - invalid scale`() {
        val name = mealName()
        val description = mealDescription()
        val price = BigDecimal("1").setScale(10)

        val result = AddMealToMenuRequest.from(
            name = name.value,
            description = description.value,
            price = price
        )

        result shouldBeLeft InvalidMealParameters("Invalid scale")
    }
}