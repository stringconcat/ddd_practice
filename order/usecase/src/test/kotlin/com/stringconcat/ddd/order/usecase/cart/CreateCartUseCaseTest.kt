package com.stringconcat.ddd.order.usecase.cart

import com.stringconcat.ddd.order.domain.cart.Cart
import com.stringconcat.ddd.order.domain.cart.CartFactory
import com.stringconcat.ddd.order.domain.cart.CartIdGenerator
import com.stringconcat.ddd.order.domain.cart.CustomerCartExtractor
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.usecase.menu.TestCartPersister
import com.stringconcat.ddd.order.usecase.menu.cartId
import io.kotest.matchers.maps.shouldContainKey
import org.junit.jupiter.api.Test
import java.util.UUID

internal class CreateCartUseCaseTest {

    @Test
    fun `successfully created`() {
        val cartPersister = TestCartPersister()
        val cartFactory = CartFactory(TestCartIdGenerator, TestCartExtractor)
        val useCase = CreateCartUseCase(cartFactory, cartPersister)

        val cartId = useCase.createOrGetCart(UUID.randomUUID().toString())
        cartPersister.shouldContainKey(cartId)
    }

    object TestCartIdGenerator : CartIdGenerator {
        override fun generate() = cartId()
    }

    object TestCartExtractor : CustomerCartExtractor {
        override fun getCartByCustomerId(customerId: CustomerId): Cart? = null
    }

}