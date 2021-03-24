package com.stringconcat.ddd.order.usecase.cart

import com.stringconcat.ddd.order.usecase.TestCartExtractor
import com.stringconcat.ddd.order.usecase.TestCartRemover
import com.stringconcat.ddd.order.usecase.cart
import com.stringconcat.ddd.order.usecase.customerId
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
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