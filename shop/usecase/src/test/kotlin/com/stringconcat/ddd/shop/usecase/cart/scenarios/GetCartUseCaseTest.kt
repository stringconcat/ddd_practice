package com.stringconcat.ddd.shop.usecase.cart.scenarios

import com.stringconcat.ddd.common.types.count
import com.stringconcat.ddd.shop.domain.cart
import com.stringconcat.ddd.shop.domain.customerId
import com.stringconcat.ddd.shop.domain.meal
import com.stringconcat.ddd.shop.usecase.MockCartExtractor
import com.stringconcat.ddd.shop.usecase.MockMealExtractor
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
        val meal = meal()

        val count = count()

        val customerId = customerId()

        val cart = cart(
            customerId = customerId,
            meals = mapOf(meal.id to count)
        )

        val cartExtractor = MockCartExtractor(cart)

        val mealExtractor = MockMealExtractor(meal)

        val useCase = GetCartUseCase(mealExtractor, cartExtractor)
        val result = useCase.execute(customerId)

        cartExtractor.verifyInvoked(cart.forCustomer)
        mealExtractor.verifyInvokedGetById(meal.id)
        val extractedCart = result.shouldBeRight()
        extractedCart.forCustomer shouldBe customerId
        extractedCart.items shouldContainExactlyInAnyOrder listOf(
            CartItem(
                mealId = meal.id,
                mealName = meal.name,
                count = count
            )
        )
    }

    @Test
    fun `cart not found`() {
        val cartExtractor = MockCartExtractor()
        val mealExtractor = MockMealExtractor()
        val useCase = GetCartUseCase(mealExtractor, cartExtractor)
        val customerId = customerId()

        val result = useCase.execute(customerId)

        cartExtractor.verifyInvoked(customerId)
        mealExtractor.verifyEmpty()
        result shouldBeLeft GetCartUseCaseError.CartNotFound
    }

    @Test
    fun `meal not found`() {

        val mealExtractor = MockMealExtractor()

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
        mealExtractor.verifyInvokedGetById(meal.id)
    }
}