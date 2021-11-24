package com.stringconcat.ddd.shop.usecase.cart.scenarios

import com.stringconcat.ddd.shop.domain.cart
import com.stringconcat.ddd.shop.domain.cart.CartIdGenerator
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.cartId
import com.stringconcat.ddd.shop.domain.customerId
import com.stringconcat.ddd.shop.domain.meal
import com.stringconcat.ddd.shop.usecase.MockCartExtractor
import com.stringconcat.ddd.shop.usecase.MockCartPersister
import com.stringconcat.ddd.shop.usecase.MockMealExtractor
import com.stringconcat.ddd.shop.usecase.cart.AddMealToCartUseCaseError
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import java.util.UUID
import org.junit.jupiter.api.Test

internal class AddMealToCartUseCaseTest {

    @Test
    fun `cart doesn't exist - successfully added`() {

        val meal = meal()
        val cartPersister = MockCartPersister()
        val cartExtractor = MockCartExtractor()
        val mealExtractor = MockMealExtractor(meal)

        val useCase = AddMealToCartUseCase(
            mealExtractor = mealExtractor,
            cartPersister = cartPersister,
            cartExtractor = cartExtractor,
            idGenerator = TestCartIdGenerator
        )

        val customerId = customerId()
        val result = useCase.execute(customerId, meal.id)

        result.shouldBeRight()
        mealExtractor.verifyInvokedGetById(meal.id)
        cartPersister.verifyInvoked(TestCartIdGenerator.id, customerId, meal.id)
    }

    @Test
    fun `cart exists - successfully added`() {

        val meal = meal()
        val customerId = customerId()
        val existingCart = cart(customerId = customerId)

        val cartPersister = MockCartPersister()
        val mealExtractor = MockMealExtractor(meal)
        val cartExtractor = MockCartExtractor(existingCart)

        val useCase = AddMealToCartUseCase(
            mealExtractor = mealExtractor,
            cartPersister = cartPersister,
            cartExtractor = cartExtractor,
            idGenerator = TestCartIdGenerator
        )

        val result = useCase.execute(customerId, meal.id)
        result.shouldBeRight()

        mealExtractor.verifyInvokedGetById(meal.id)
        cartPersister.verifyInvoked(existingCart, meal.id)
        cartExtractor.verifyInvoked(existingCart.forCustomer)
    }

    @Test
    fun `meal not found`() {

        val meal = meal()
        val cartPersister = MockCartPersister()
        val cartExtractor = MockCartExtractor()
        val mealExtractor = MockMealExtractor()

        val useCase = AddMealToCartUseCase(
            mealExtractor = mealExtractor,
            cartPersister = cartPersister,
            cartExtractor = cartExtractor,
            idGenerator = TestCartIdGenerator
        )

        val result = useCase.execute(CustomerId(UUID.randomUUID().toString()), meal.id)

        mealExtractor.verifyInvokedGetById(meal.id)
        cartPersister.verifyEmpty()
        cartExtractor.verifyEmpty()
        result shouldBeLeft AddMealToCartUseCaseError.MealNotFound
    }

    object TestCartIdGenerator : CartIdGenerator {
        val id = cartId()
        override fun generate() = id
    }
}