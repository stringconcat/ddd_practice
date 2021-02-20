package com.stringconcat.ddd.order.domain.menu

import com.stringconcat.ddd.order.domain.mealDescription
import com.stringconcat.ddd.order.domain.mealId
import com.stringconcat.ddd.order.domain.mealName
import com.stringconcat.ddd.order.domain.price
import com.stringconcat.ddd.order.domain.version
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class MealRestorerTest {

    @Test
    fun `restore meal - success`() {

        val mealId = mealId()
        val price = price()
        val name = mealName()
        val description = mealDescription()
        val removed = true
        val version = version()

        val meal: Meal = MealRestorer.restoreMeal(
            id = mealId,
            name = name,
            removed = removed,
            description = description,
            price = price,
            version = version
        )

        meal.id shouldBe mealId
        meal.name shouldBe name
        meal.description shouldBe description
        meal.price shouldBe price
        meal.removed shouldBe removed
        meal.version shouldBe version
        meal.popEvents().shouldBeEmpty()
    }
}