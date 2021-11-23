package com.stringconcat.ddd.shop.usecase.cart.scenarios

import com.stringconcat.ddd.shop.domain.cart
import com.stringconcat.ddd.shop.domain.mealId
import com.stringconcat.ddd.shop.usecase.MockCartExtractor
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
        val cartExtractor = MockCartExtractor(cart)

        val useCase = RemoveMealFromCartUseCase(cartExtractor, cartPersister)
        val result = useCase.execute(cart.forCustomer, mealId())

        cartExtractor.verifyInvoked(cart.forCustomer)
        cartPersister.verifyInvoked(cart)
        result.shouldBeRight()
    }

    @Test
    fun `cart not found`() {

        val cart = cart()
        val cartPersister = MockCartPersister()
        val cartExtractor = MockCartExtractor()

        val useCase = RemoveMealFromCartUseCase(cartExtractor, cartPersister)
        val result = useCase.execute(cart.forCustomer, mealId())

        cartPersister.verifyEmpty()
        cartExtractor.verifyInvoked(cart.forCustomer)
        result shouldBeLeft RemoveMealFromCartUseCaseError.CartNotFound
    }
}