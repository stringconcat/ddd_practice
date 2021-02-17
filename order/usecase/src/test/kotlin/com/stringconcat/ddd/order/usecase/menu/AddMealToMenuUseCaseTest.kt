package com.stringconcat.ddd.order.usecase.menu

import arrow.core.Either
import com.stringconcat.ddd.order.domain.menu.Meal
import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.domain.menu.MealIdGenerator
import com.stringconcat.ddd.order.domain.menu.MealName
import com.stringconcat.ddd.order.domain.rules.MealAlreadyExistsRule
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class AddMealToMenuUseCaseTest {

    @Test
    fun test() {
        val result = AddMealToMenuUseCase(
            FakeMealPersister,
            FakeMealIdGenerator,
            MealExist
        ).addMealToMenu(
            AddMealToMenuRequest(
                "cacke",
                "my favorite cakce",
                BigDecimal.ONE
            )
        )

        assertTrue(result is Either.Left)

        result as Either.Left
        println(result)
        assertTrue(result.a is AddMealToMenuUseCaseError.AlreadyExists)


    }
}

object FakeMealPersister: MealPersister {
    override fun save(meal: Meal) {}
}

object FakeMealIdGenerator: MealIdGenerator {
    override fun generateId(): MealId = MealId(42)
}

object MealExist: MealAlreadyExistsRule {
    override fun exists(name: MealName) = true

}