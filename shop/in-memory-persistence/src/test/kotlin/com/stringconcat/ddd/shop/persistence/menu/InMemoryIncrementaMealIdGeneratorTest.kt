package com.stringconcat.ddd.shop.persistence.menu

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class InMemoryIncrementaMealIdGeneratorTest {

    @Test
    fun `id is incremented`() {
        val generator = InMemoryIncrementalMealIdGenerator()
        val mealId1 = generator.generate()
        val mealId2 = generator.generate()
        mealId1.toLongValue() shouldBe mealId2.toLongValue() - 1
    }
}