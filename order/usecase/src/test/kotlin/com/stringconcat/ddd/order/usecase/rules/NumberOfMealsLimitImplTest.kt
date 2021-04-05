package com.stringconcat.ddd.order.usecase.rules

import com.stringconcat.ddd.common.types.common.Count
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class NumberOfMealsLimitImplTest {

    @Test
    fun `should return correct limit`() {
        val limitation = Count.from(42).orNull().shouldNotBeNull()
        val rule = NumberOfMealsLimitImpl { limitation }
        rule.maximumNumberOfMeals() shouldBe limitation
    }
}