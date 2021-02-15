package com.stringconcat.ddd.order.domain.meal

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class MealRestorerTest {

    @Test
    fun `restore meal`() {

        val mealId = mealId()
        val price = price()
        val name = mealName()
        val description = mealDescription()
        val address = address()
        val removed = true
        val version = version()

        val meal: Meal = MealRestorer.restoreMeal(
            id = mealId,
            name = name,
            removed = removed,
            description = description,
            address = address,
            price = price,
            version = version
        )

        meal.address shouldBe address
        meal.id shouldBe mealId
        meal.name shouldBe name
        meal.description shouldBe description
        meal.price shouldBe price
        meal.removed shouldBe removed
        meal.version shouldBe version
        meal.popEvents() shouldContainExactly emptyList()
    }
}