package com.stringconcat.ddd.order.usecase.cart

import com.stringconcat.ddd.order.usecase.TestCartExtractor
import com.stringconcat.ddd.order.usecase.TestMealExtractor
import com.stringconcat.ddd.order.usecase.cart
import com.stringconcat.ddd.order.usecase.count
import com.stringconcat.ddd.order.usecase.customerId
import com.stringconcat.ddd.order.usecase.meal
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.lang.IllegalStateException

class GetCartUseCaseTest {

    @Test
    fun `cart successfully extracted`() {
        val meal1 = meal()
        val meal2 = meal()

        val count1 = count()
        val count2 = count()

        val customerId = customerId()

        val cart = cart(
            customerId = customerId,
            meals = mapOf(meal1.id to count1, meal2.id to count2)
        )

        val cartExtractor = TestCartExtractor().apply {
            this[customerId] = cart
        }

        val mealExtractor = TestMealExtractor().apply {
            this[meal1.id] = meal1
            this[meal2.id] = meal2
        }

        val useCase = GetCartUseCase(mealExtractor, cartExtractor)
        val result = useCase.execute(customerId)
        result shouldBeRight {
            it.customerId shouldBe customerId
            it.items shouldContainExactlyInAnyOrder listOf(
                CartItem(
                    mealId = meal1.id,
                    mealName = meal1.name,
                    count = count1
                ),
                CartItem(
                    mealId = meal2.id,
                    mealName = meal2.name,
                    count = count2
                )
            )
        }
    }

    @Test
    fun `cart not found`() {
        val cartExtractor = TestCartExtractor()
        val mealExtractor = TestMealExtractor()
        val useCase = GetCartUseCase(mealExtractor, cartExtractor)

        val result = useCase.execute(customerId())
        result shouldBeLeft GetCartUseCaseError.CartNotFound
    }

    @Test
    fun `meal not found`() {

        val mealExtractor = TestMealExtractor()

        val customerId = customerId()
        val meal = meal()

        val cart = cart(
            customerId = customerId,
            meals = mapOf(meal.id to count())
        )

        val cartExtractor = TestCartExtractor().apply {
            this[customerId] = cart
        }

        val useCase = GetCartUseCase(mealExtractor, cartExtractor)

        shouldThrow<IllegalStateException> {
            useCase.execute(customerId)
        }
    }
}