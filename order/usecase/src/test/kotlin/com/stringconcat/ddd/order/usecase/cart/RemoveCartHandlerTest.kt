package com.stringconcat.ddd.order.usecase.cart

import com.stringconcat.ddd.order.domain.cart.Cart
import com.stringconcat.ddd.order.domain.cart.CartId
import com.stringconcat.ddd.order.usecase.menu.TestCartExtractor
import com.stringconcat.ddd.order.usecase.menu.TestCartRemover
import com.stringconcat.ddd.order.usecase.menu.cart
import com.stringconcat.ddd.order.usecase.menu.customerId
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import org.junit.jupiter.api.Test

internal class RemoveCartHandlerTest {

    private val cartRemover = TestCartRemover()

    @Test
    fun `successfully removed`() {
        val cart = cart()
        val cartExtractor = TestCartExtractor().apply {
            this[cart.customerId] = cart
        }
        val handler = RemoveCartHandler(cartExtractor, cartRemover)
        val result = handler.removeCart(cart.customerId.value)
        result.shouldBeRight()
        cartRemover.deleted shouldContainExactly listOf(cart.id)
    }

    @Test
    fun `cart not found`() {
        val cartExtractor = TestCartExtractor()
        val handler = RemoveCartHandler(cartExtractor, cartRemover)
        val result = handler.removeCart(customerId().value)
        result shouldBeLeft RemoveCartHandlerError.CartNotFound
        cartRemover.deleted.shouldBeEmpty()
    }
}