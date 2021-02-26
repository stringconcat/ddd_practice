package com.stringconcat.ddd.order.usecase.cart

import com.stringconcat.ddd.order.usecase.TestCartExtractor
import com.stringconcat.ddd.order.usecase.TestCartPersister
import com.stringconcat.ddd.order.usecase.cart
import com.stringconcat.ddd.order.usecase.mealId
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContainExactly
import org.junit.jupiter.api.Test

class RemoveMealFromCartUseCaseTest {

    private val cart = cart()
    private val cartPersister = TestCartPersister()
    private val cartExtractor = TestCartExtractor().apply {
        this[cart.customerId] = cart
    }

    @Test
    fun `successfully removed`() {
        val useCase = RemoveMealFromCartUseCase(cartExtractor, cartPersister)
        val result = useCase.execute(cart.customerId.value, mealId().value)
        result.shouldBeRight()
        cartPersister shouldContainExactly mapOf(cart.customerId to cart)
    }

    @Test
    fun `cart not found`() {
        cartExtractor.clear()
        val useCase = RemoveMealFromCartUseCase(cartExtractor, cartPersister)
        val result = useCase.execute(cart.customerId.value, mealId().value)
        result shouldBeLeft RemoveMealFromCartCaseError.CartNotFound
        cartPersister.shouldBeEmpty()
    }
}