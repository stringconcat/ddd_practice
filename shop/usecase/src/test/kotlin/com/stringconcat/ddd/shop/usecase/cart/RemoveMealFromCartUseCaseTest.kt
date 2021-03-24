package com.stringconcat.ddd.shop.usecase.cart

import com.stringconcat.ddd.shop.usecase.TestCartExtractor
import com.stringconcat.ddd.shop.usecase.TestCartPersister
import com.stringconcat.ddd.shop.usecase.cart
import com.stringconcat.ddd.shop.usecase.mealId
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContainExactly
import org.junit.jupiter.api.Test

class RemoveMealFromCartUseCaseTest {

    @Test
    fun `successfully removed`() {

        val cart = cart()
        val cartPersister = TestCartPersister()
        val cartExtractor = TestCartExtractor().apply {
            this[cart.forCustomer] = cart
        }

        val useCase = RemoveMealFromCartUseCase(cartExtractor, cartPersister)
        val result = useCase.execute(cart.forCustomer, mealId())
        result.shouldBeRight()
        cartPersister shouldContainExactly mapOf(cart.forCustomer to cart)
    }

    @Test
    fun `cart not found`() {

        val cart = cart()
        val cartPersister = TestCartPersister()
        val cartExtractor = TestCartExtractor()

        val useCase = RemoveMealFromCartUseCase(cartExtractor, cartPersister)
        val result = useCase.execute(cart.forCustomer, mealId())
        result shouldBeLeft RemoveMealFromCartUseCaseError.CartNotFound
        cartPersister.shouldBeEmpty()
    }
}