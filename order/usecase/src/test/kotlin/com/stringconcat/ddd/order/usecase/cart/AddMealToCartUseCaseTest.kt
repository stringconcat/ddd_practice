package com.stringconcat.ddd.order.usecase.cart

import com.stringconcat.ddd.order.domain.cart.CartFactory
import com.stringconcat.ddd.order.domain.cart.CartIdGenerator
import com.stringconcat.ddd.order.usecase.menu.TestCartExtractor
import com.stringconcat.ddd.order.usecase.menu.TestCartPersister
import com.stringconcat.ddd.order.usecase.menu.TestMealExtractor
import com.stringconcat.ddd.order.usecase.menu.cartId
import com.stringconcat.ddd.order.usecase.menu.customerId
import com.stringconcat.ddd.order.usecase.menu.meal
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContainKey
import org.junit.jupiter.api.Test
import java.util.UUID

internal class AddMealToCartUseCaseTest {

    private val meal = meal()
    private val cartPersister = TestCartPersister()
    private val cartExtractor = TestCartExtractor()
    private val cartFactory = CartFactory(TestCartIdGenerator, cartExtractor)
    private val mealExtractor = TestMealExtractor().apply {
        this[meal.id] = meal
    }

    @Test
    fun `successfully added`() {

        val useCase = AddMealToCartUseCase(
            mealExtractor = mealExtractor,
            cartFactory = cartFactory,
            cartPersister = cartPersister
        )

        val customerId = customerId()
        val result = useCase.addMealToCart(customerId.value, meal.id.value)
        result.shouldBeRight()
        cartPersister shouldContainKey customerId
        cartPersister[customerId]
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
        cartPersister.shouldBeEmpty()
    }

    object TestCartIdGenerator : CartIdGenerator {
        override fun generate() = cartId()
    }
}