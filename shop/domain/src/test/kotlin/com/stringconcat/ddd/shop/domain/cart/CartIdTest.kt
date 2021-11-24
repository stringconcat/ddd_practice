package com.stringconcat.ddd.shop.domain.cart

import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class CartIdTest {

    @Test
    fun `check equality`() {
        val id = Random.nextLong()

        val shopOrderId1 = ShopOrderId(id)
        val shopOrderId2 = ShopOrderId(id)
        shopOrderId1 shouldBe shopOrderId2
        shopOrderId1 shouldNotBeSameInstanceAs shopOrderId2
        shopOrderId1.toLongValue() shouldBe shopOrderId2.toLongValue()
    }
}