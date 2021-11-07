package com.stringconcat.ddd.shop.usecase.cart.scenarios

import com.stringconcat.ddd.shop.usecase.TestCartExtractor
import com.stringconcat.ddd.shop.usecase.TestCartRemover
import com.stringconcat.ddd.shop.usecase.cart
import com.stringconcat.ddd.shop.usecase.cart.RemoveCartHandlerError
import com.stringconcat.ddd.shop.usecase.customerId
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import org.junit.jupiter.api.Test

internal class RemoveCartHandlerTest {

    @Test
    fun `successfully removed`() {

        val cartRemover = TestCartRemover()
        val cart = cart()
        val cartExtractor = TestCartExtractor().apply {
            this[cart.forCustomer] = cart
        }
        val handler = RemoveCartHandler(cartExtractor, cartRemover)
        val result = handler.execute(cart.forCustomer)
        result.shouldBeRight()
        cartRemover.deleted shouldContainExactly listOf(cart.id)
    }

    @Test
    fun `cart not found`() {

        val cartRemover = TestCartRemover()
        val cartExtractor = TestCartExtractor()
        val handler = RemoveCartHandler(cartExtractor, cartRemover)
        val result = handler.execute(customerId())
        result shouldBeLeft RemoveCartHandlerError.CartNotFound
        cartRemover.deleted.shouldBeEmpty()
    }
}