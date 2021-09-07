package com.stringconcat.ddd.shop.usecase.cart.scenarios

import com.stringconcat.ddd.shop.domain.cart.CartIdGenerator
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.usecase.TestCartExtractor
import com.stringconcat.ddd.shop.usecase.TestCartPersister
import com.stringconcat.ddd.shop.usecase.TestMealExtractor
import com.stringconcat.ddd.shop.usecase.cart
import com.stringconcat.ddd.shop.usecase.cart.AddMealToCartUseCaseError
import com.stringconcat.ddd.shop.usecase.cartId
import com.stringconcat.ddd.shop.usecase.count
import com.stringconcat.ddd.shop.usecase.customerId
import com.stringconcat.ddd.shop.usecase.meal
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.junit.jupiter.api.Test
import java.util.UUID

internal class AddMealToCartUseCaseTest {

    @Test
    fun `cart doesn't exist - successfully added`() {

        val meal = meal()
        val cartPersister = TestCartPersister()
        val cartExtractor = TestCartExtractor()
        val mealExtractor = TestMealExtractor().apply {
            this[meal.id] = meal
        }

        val useCase = AddMealToCartUseCase(
            mealExtractor = mealExtractor,
            cartPersister = cartPersister,
            cartExtractor = cartExtractor,
            idGenerator = TestCartIdGenerator
        )

        val customerId = customerId()
        val result = useCase.execute(customerId, meal.id)
        result.shouldBeRight()
        cartPersister shouldContainKey customerId
        val cart = cartPersister[customerId]

        cart.shouldNotBeNull()
        cart.id shouldBe TestCartIdGenerator.id
        cart.forCustomer shouldBe customerId
        cart.meals() shouldContainExactly mapOf(meal.id to count(1))
    }

    @Test
    fun `cart exists - successfully added`() {

        val meal = meal()
        val customerId = customerId()
        val existingCart = cart(customerId = customerId)

        val cartPersister = TestCartPersister()
        val mealExtractor = TestMealExtractor().apply {
            this[meal.id] = meal
        }
        val cartExtractor = TestCartExtractor().apply {
            this[customerId] = existingCart
        }

        val useCase = AddMealToCartUseCase(
            mealExtractor = mealExtractor,
            cartPersister = cartPersister,
            cartExtractor = cartExtractor,
            idGenerator = TestCartIdGenerator
        )

        val result = useCase.execute(customerId, meal.id)
        result.shouldBeRight()
        cartPersister shouldContainKey customerId
        val cart = cartPersister[customerId]

        cart.shouldNotBeNull()
        cart shouldBeSameInstanceAs existingCart
        cart.meals() shouldContainExactly mapOf(meal.id to count(1))
    }

    @Test
    fun `meal not found`() {

        val meal = meal()
        val cartPersister = TestCartPersister()
        val cartExtractor = TestCartExtractor()
        val mealExtractor = TestMealExtractor()

        val useCase = AddMealToCartUseCase(
            mealExtractor = mealExtractor,
            cartPersister = cartPersister,
            cartExtractor = cartExtractor,
            idGenerator = TestCartIdGenerator
        )

        val result = useCase.execute(CustomerId(UUID.randomUUID().toString()), meal.id)
        result shouldBeLeft AddMealToCartUseCaseError.MealNotFound
        cartPersister.shouldBeEmpty()
    }

    object TestCartIdGenerator : CartIdGenerator {
        val id = cartId()
        override fun generate() = id
    }
}