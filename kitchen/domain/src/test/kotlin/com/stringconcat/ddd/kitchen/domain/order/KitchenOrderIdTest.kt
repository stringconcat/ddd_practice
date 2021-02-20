package com.stringconcat.ddd.kitchen.domain.order

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import org.junit.jupiter.api.Test
import kotlin.random.Random

class KitchenOrderIdTest {

    @Test
    fun `check equality`() {
        val id = Random.nextLong()

        val kitchenOrderId1 = KitchenOrderId(id)
        val kitchenOrderId2 = KitchenOrderId(id)
        kitchenOrderId1 shouldBe kitchenOrderId2
        kitchenOrderId1 shouldNotBeSameInstanceAs kitchenOrderId2
        kitchenOrderId1.value shouldBe kitchenOrderId2.value
    }
}