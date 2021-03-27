package com.stringconcat.ddd.order.usecase.rules

import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.order.domain.cart.Cart
import com.stringconcat.ddd.order.usecase.cart
import com.stringconcat.ddd.order.usecase.mealId
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class NumberOfMealsExceedsLimitImplTest {

    val cartWith2Meals = cart(mapOf(mealId() to Count.one(), mealId() to Count.one()))

    @Test
    fun `number of meals less than the limit`() {
        testLimitationRule(cart = cartWith2Meals, limit = 3, expectedResult = false)
    }

    @Test
    fun `number of meals same as the limit`() {
        testLimitationRule(cart = cartWith2Meals, limit = 2, expectedResult = false)

    }

    @Test
    fun `number of meals exceeds the limit`() {
        testLimitationRule(cart = cartWith2Meals, limit = 1, expectedResult = true)
    }

    private fun testLimitationRule(cart: Cart, limit: Int, expectedResult: Boolean) {
        val limitation = Count.from(limit).orNull().shouldNotBeNull()

        val rule = NumberOfMealsExceedsLimitImpl(limitation)

        rule.check(cart) shouldBe expectedResult
    }
}