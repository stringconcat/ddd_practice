package com.stringconcat.ddd.order.usecase.cart

import com.stringconcat.ddd.order.domain.cart.Cart
import com.stringconcat.ddd.order.domain.cart.CartIdGenerator
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.cart.NumberOfMealsExceedsLimit
import com.stringconcat.ddd.order.usecase.TestCartExtractor
import com.stringconcat.ddd.order.usecase.TestCartPersister
import com.stringconcat.ddd.order.usecase.TestMealExtractor
import com.stringconcat.ddd.order.usecase.cart
import com.stringconcat.ddd.order.usecase.cartId
import com.stringconcat.ddd.order.usecase.count
import com.stringconcat.ddd.order.usecase.customerId
import com.stringconcat.ddd.order.usecase.meal
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
            idGenerator = TestCartIdGenerator,
            numberOfMealsExceedsLimit = NumberOfMealsDoesNotExceedLimit
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
            idGenerator = TestCartIdGenerator,
            numberOfMealsExceedsLimit = NumberOfMealsDoesNotExceedLimit
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
            idGenerator = TestCartIdGenerator,
            numberOfMealsExceedsLimit = NumberOfMealsDoesNotExceedLimit
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

object NumberOfMealsDoesNotExceedLimit : NumberOfMealsExceedsLimit {
    override fun check(cart: Cart): Boolean {
        return false
    }
}
