package com.stringconcat.ddd.shop.domain.cart

import com.stringconcat.ddd.common.types.count
import com.stringconcat.ddd.shop.domain.cartId
import com.stringconcat.ddd.shop.domain.customerId
import com.stringconcat.ddd.shop.domain.mealId
import com.stringconcat.ddd.shop.domain.version
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

class CartRestorerTest {

    @Test
    fun `restore cart - success`() {
        val cartId = cartId()
        val guestId = customerId()
        val version = version()
        val meals = mapOf(mealId() to count())
        val created = OffsetDateTime.now()
        val cart = CartRestorer.restoreCart(cartId, guestId, created, meals, version)

        cart.id shouldBe cartId
        cart.forCustomer shouldBe guestId
        cart.version shouldBe version
        cart.created shouldBe created
        cart.meals() shouldContainExactly meals
    }
}