package com.stringconcat.ddd.order.domain.cart

import com.stringconcat.ddd.order.domain.cart
import com.stringconcat.ddd.order.domain.customerId
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.date.shouldBeBefore
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import kotlin.random.Random

internal class CartFactoryTest {

    val id = CartId(Random.nextLong())

    private val idGenerator = object : CartIdGenerator {
        override fun generate() = id
    }

    @Test
    fun `create cart - cart doesn't exist`() {
        val getCartByGuestId = HashMapGetCartByGuestId()
        val factory = CartFactory(idGenerator, getCartByGuestId)
        val guestId = customerId()
        val cart = factory.createOrGetCartForGuest(guestId)

        cart.id shouldBe id
        cart.meals() shouldContainExactly emptyMap()
        cart.created shouldBeBefore OffsetDateTime.now()
        cart.popEvents() shouldContainExactly listOf(CartHasBeenCreatedEvent(id))
    }

    @Test
    fun `create cart - cart exists`() {
        val getCartByGuestId = HashMapGetCartByGuestId()
        val cart = cart()
        getCartByGuestId[cart.customerId] = cart

        val factory = CartFactory(idGenerator, getCartByGuestId)
        val result = factory.createOrGetCartForGuest(cart.customerId)
        result shouldBeSameInstanceAs cart
        result.popEvents() shouldContainExactly emptyList()
    }

    private class HashMapGetCartByGuestId : GetCartByGuestId, HashMap<CustomerId, Cart>() {
        override fun getCartByGuestId(customerId: CustomerId): Cart? {
            return this[customerId]
        }
    }
}