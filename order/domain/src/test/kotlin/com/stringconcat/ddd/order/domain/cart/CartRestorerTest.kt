package com.stringconcat.ddd.order.domain.cart

import com.stringconcat.ddd.order.domain.cartId
import com.stringconcat.ddd.order.domain.count
import com.stringconcat.ddd.order.domain.guestId
import com.stringconcat.ddd.order.domain.mealId
import com.stringconcat.ddd.order.domain.version
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

class CartRestorerTest {

    @Test
    fun `restore cart - success`() {
        val cartId = cartId()
        val guestId = guestId()
        val version = version()
        val meals = mapOf(mealId() to count())
        val created = OffsetDateTime.now()
        val cart = CartRestorer.restoreCart(cartId, guestId, created, meals, version)

        cart.id shouldBe cartId
        cart.customerId shouldBe guestId
        cart.version shouldBe version
        cart.created shouldBe created
        cart.meals() shouldContainExactly meals
    }
}