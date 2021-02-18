package com.stringconcat.ddd.order.usecase.menu

import com.stringconcat.ddd.order.domain.menu.Meal
import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.domain.menu.MealIdGenerator
import com.stringconcat.ddd.order.domain.menu.MealName
import com.stringconcat.ddd.order.domain.rules.MealAlreadyExistsRule
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class AddMealToMenuUseCaseTest {

    val persister = TestMealPersister()

    @Test
    fun `successfully added`() {

        val name = mealName()
        val description = mealDescription()
        val price = price()

        val result = AddMealToMenuUseCase(
            persister,
            TestMealIdGenerator,
            MealNotExist
        ).addMealToMenu(
            AddMealToMenuRequest(
                name.value,
                description.value,
                price.value
            )
        )

        val id = TestMealIdGenerator.id

        result shouldBeRight {
            it shouldBe id
        }

        val meal = persister[id]
        meal.shouldNotBeNull()

        meal.id shouldBe id
        meal.name shouldBe name
        meal.description shouldBe description
        meal.price shouldBe price
    }

    @Test
    fun `meal already exists`() {

        val name = mealName()
        val description = mealDescription()
        val price = price()

        val result = AddMealToMenuUseCase(
            persister,
            TestMealIdGenerator,
            MealExist
        ).addMealToMenu(
            AddMealToMenuRequest(
                name.value,
                description.value,
                price.value
            )
        )

        result shouldBeLeft AddMealToMenuUseCaseError.AlreadyExists
        persister shouldContainExactly emptyMap()
    }

    @Test
    fun `invalid name`() {

        val name = ""
        val description = mealDescription()
        val price = price()

        val result = AddMealToMenuUseCase(
            persister,
            TestMealIdGenerator,
            MealExist
        ).addMealToMenu(
            AddMealToMenuRequest(
                name,
                description.value,
                price.value
            )
        )

        result shouldBeLeft AddMealToMenuUseCaseError.InvalidName("Empty name")
        persister shouldContainExactly emptyMap()
    }

    @Test
    fun `invalid description`() {
        val name = mealName()
        val description = ""
        val price = price()

        val result = AddMealToMenuUseCase(
            persister,
            TestMealIdGenerator,
            MealExist
        ).addMealToMenu(
            AddMealToMenuRequest(
                name.value,
                description,
                price.value
            )
        )

        result shouldBeLeft AddMealToMenuUseCaseError.InvalidDescription("Empty description")
        persister shouldContainExactly emptyMap()
    }

    @Test
    fun `invalid price - negative value`() {
        val name = mealName()
        val description = mealDescription()
        val price = BigDecimal("-1")

        val result = AddMealToMenuUseCase(
            persister,
            TestMealIdGenerator,
            MealExist
        ).addMealToMenu(
            AddMealToMenuRequest(
                name.value,
                description.value,
                price
            )
        )

        result shouldBeLeft AddMealToMenuUseCaseError.InvalidPrice("Negative value")
        persister shouldContainExactly emptyMap()
    }

    @Test
    fun `invalid price - invalid scale`() {
        val name = mealName()
        val description = mealDescription()
        val price = BigDecimal("1").setScale(10)

        val result = AddMealToMenuUseCase(
            persister,
            TestMealIdGenerator,
            MealExist
        ).addMealToMenu(
            AddMealToMenuRequest(
                name.value,
                description.value,
                price
            )
        )

        result shouldBeLeft AddMealToMenuUseCaseError.InvalidPrice("Invalid scale")
        persister shouldContainExactly emptyMap()
    }


    object TestMealIdGenerator : MealIdGenerator {
        val id = mealId()
        override fun generateId() = id
    }

    object MealExist : MealAlreadyExistsRule {
        override fun exists(name: MealName) = true
    }

    object MealNotExist : MealAlreadyExistsRule {
        override fun exists(name: MealName) = false
    }
}