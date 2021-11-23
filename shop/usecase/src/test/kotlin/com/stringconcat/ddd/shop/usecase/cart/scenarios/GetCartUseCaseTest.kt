package com.stringconcat.ddd.shop.usecase.cart.scenarios

import com.stringconcat.ddd.common.types.count
import com.stringconcat.ddd.shop.domain.cart
import com.stringconcat.ddd.shop.domain.customerId
import com.stringconcat.ddd.shop.domain.meal
import com.stringconcat.ddd.shop.usecase.MockCartExtractor
import com.stringconcat.ddd.shop.usecase.TestMealExtractor
import com.stringconcat.ddd.shop.usecase.cart.CartItem
import com.stringconcat.ddd.shop.usecase.cart.GetCartUseCaseError
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

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

        val cartExtractor = MockCartExtractor(cart)

        val mealExtractor = TestMealExtractor().apply {
            this[meal1.id] = meal1
            this[meal2.id] = meal2
        }

        val useCase = GetCartUseCase(mealExtractor, cartExtractor)
        val result = useCase.execute(customerId)

        cartExtractor.verifyInvoked(cart.forCustomer)
        val extractedCart = result.shouldBeRight()
        extractedCart.forCustomer shouldBe customerId
        extractedCart.items shouldContainExactlyInAnyOrder listOf(
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

    @Test
    fun `cart not found`() {
        val cartExtractor = MockCartExtractor()
        val mealExtractor = TestMealExtractor()
        val useCase = GetCartUseCase(mealExtractor, cartExtractor)
        val customerId = customerId()

        val result = useCase.execute(customerId)

        cartExtractor.verifyInvoked(customerId)
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

        val cartExtractor = MockCartExtractor(cart)

        val useCase = GetCartUseCase(mealExtractor, cartExtractor)

        cartExtractor.verifyEmpty()
        shouldThrow<IllegalStateException> {
            useCase.execute(customerId)
        }
    }
}