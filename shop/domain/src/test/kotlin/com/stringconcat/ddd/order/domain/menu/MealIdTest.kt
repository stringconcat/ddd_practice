package com.stringconcat.ddd.order.domain.menu

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class MealIdTest {

    @Test
    fun `check equality`() {
        val id = Random.nextLong()

        val mealId1 = MealId(id)
        val mealId2 = MealId(id)
        mealId1 shouldBe mealId2
        mealId1 shouldNotBeSameInstanceAs mealId2
        mealId1.value shouldBe mealId2.value
    }
}