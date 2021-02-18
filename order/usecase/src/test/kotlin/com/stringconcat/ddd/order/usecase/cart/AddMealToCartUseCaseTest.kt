package com.stringconcat.ddd.order.usecase.cart

import com.stringconcat.ddd.order.domain.cart.Cart
import com.stringconcat.ddd.order.domain.cart.CartFactory
import com.stringconcat.ddd.order.domain.cart.CartIdGenerator
import com.stringconcat.ddd.order.domain.cart.CustomerCartExtractor
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.usecase.menu.TestCartPersister
import com.stringconcat.ddd.order.usecase.menu.TestMealExtractor
import com.stringconcat.ddd.order.usecase.menu.cartId
import com.stringconcat.ddd.order.usecase.menu.meal
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import org.junit.jupiter.api.Test
import java.util.UUID

internal class AddMealToCartUseCaseTest {

    val meal = meal()
    val cartPersister = TestCartPersister()
    val cartFactory = CartFactory(TestCartIdGenerator, TestCartExtractor)
    val mealExtractor = TestMealExtractor().apply {
        this[meal.id] = meal
    }

    @Test
    fun `successfully added`() {

        val useCase = AddMealToCartUseCase(
            mealExtractor = mealExtractor,
            cartFactory = cartFactory,
            cartPersister = cartPersister
        )

        val result = useCase.addMealToCart(UUID.randomUUID().toString(), meal.id.value)
        result.shouldBeRight()
    }

    @Test
    fun `meal not found`() {

        mealExtractor.clear()
        val useCase = AddMealToCartUseCase(
            mealExtractor = mealExtractor,
            cartFactory = cartFactory,
            cartPersister = cartPersister
        )

        val result = useCase.addMealToCart(UUID.randomUUID().toString(), meal.id.value)
        result shouldBeLeft AddMealToCartUseCaseError.MealNotFound
    }

    object TestCartIdGenerator : CartIdGenerator {
        override fun generate() = cartId()
    }

    object TestCartExtractor : CustomerCartExtractor {
        override fun getCartByCustomerId(customerId: CustomerId): Cart? = null
    }
}