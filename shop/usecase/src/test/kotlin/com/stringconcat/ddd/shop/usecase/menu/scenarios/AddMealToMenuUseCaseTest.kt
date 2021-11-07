package com.stringconcat.ddd.shop.usecase.menu.scenarios

import com.stringconcat.ddd.shop.domain.menu.MealAlreadyExists
import com.stringconcat.ddd.shop.domain.menu.MealIdGenerator
import com.stringconcat.ddd.shop.domain.menu.MealName
import com.stringconcat.ddd.shop.usecase.TestMealPersister
import com.stringconcat.ddd.shop.usecase.mealDescription
import com.stringconcat.ddd.shop.usecase.mealId
import com.stringconcat.ddd.shop.usecase.mealName
import com.stringconcat.ddd.shop.usecase.menu.AddMealToMenuRequest
import com.stringconcat.ddd.shop.usecase.menu.AddMealToMenuUseCaseError
import com.stringconcat.ddd.shop.usecase.price
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class AddMealToMenuUseCaseTest {

    @Test
    fun `successfully added`() {

        val name = mealName()
        val description = mealDescription()
        val price = price()

        val persister = TestMealPersister()

        val result = AddMealToMenuUseCase(
            mealPersister = persister,
            idGenerator = TestMealIdGenerator,
            mealExists = MealNotExist
        ).execute(
            AddMealToMenuRequest(
                name = name,
                description = description,
                price = price
            )
        )

        val id = TestMealIdGenerator.id

        result.shouldBeRight().should {
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

        val persister = TestMealPersister()

        val result = AddMealToMenuUseCase(
            mealPersister = persister,
            idGenerator = TestMealIdGenerator,
            mealExists = MealExist
        ).execute(
            AddMealToMenuRequest(
                name = name,
                description = description,
                price = price
            )
        )

        result shouldBeLeft AddMealToMenuUseCaseError.AlreadyExists
        persister.shouldBeEmpty()
    }

    object TestMealIdGenerator : MealIdGenerator {
        val id = mealId()
        override fun generate() = id
    }

    object MealExist : MealAlreadyExists {
        override fun check(name: MealName) = true
    }

    object MealNotExist : MealAlreadyExists {
        override fun check(name: MealName) = false
    }
}