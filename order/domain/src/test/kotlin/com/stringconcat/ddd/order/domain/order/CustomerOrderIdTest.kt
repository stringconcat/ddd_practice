package com.stringconcat.ddd.order.domain.order

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class CustomerOrderIdTest {

    @Test
    fun `check equality`() {
        val id = Random.nextLong()

        val customerOrderId1 = CustomerOrderId(id)
        val customerOrderId2 = CustomerOrderId(id)
        customerOrderId1 shouldBe customerOrderId2
        customerOrderId1 shouldNotBeSameInstanceAs customerOrderId2
        customerOrderId1.value shouldBe customerOrderId2.value
    }
}