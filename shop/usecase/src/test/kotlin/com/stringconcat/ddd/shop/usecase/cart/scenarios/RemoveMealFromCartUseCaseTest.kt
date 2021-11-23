package com.stringconcat.ddd.shop.usecase.cart.scenarios

import com.stringconcat.ddd.shop.domain.cart
import com.stringconcat.ddd.shop.domain.mealId
import com.stringconcat.ddd.shop.usecase.TestCartExtractor
import com.stringconcat.ddd.shop.usecase.MockCartPersister
import com.stringconcat.ddd.shop.usecase.cart.RemoveMealFromCartUseCaseError
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import org.junit.jupiter.api.Test

class RemoveMealFromCartUseCaseTest {

    @Test
    fun `successfully removed`() {

        val cart = cart()
        val cartPersister = MockCartPersister()
        val cartExtractor = TestCartExtractor().apply {
            this[cart.forCustomer] = cart
        }

        val useCase = RemoveMealFromCartUseCase(cartExtractor, cartPersister)
        val result = useCase.execute(cart.forCustomer, mealId())
        result.shouldBeRight()
        cartPersister.verifyInvoked(cart)
    }

    @Test
    fun `cart not found`() {

        val cart = cart()
        val cartPersister = MockCartPersister()
        val cartExtractor = TestCartExtractor()

        val useCase = RemoveMealFromCartUseCase(cartExtractor, cartPersister)
        val result = useCase.execute(cart.forCustomer, mealId())
        result shouldBeLeft RemoveMealFromCartUseCaseError.CartNotFound
        cartPersister.verifyEmpty()
    }
}