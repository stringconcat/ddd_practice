package com.stringconcat.ddd.shop.persistence.order

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class InMemoryIncrementalCustomerOrderIdGeneratorTest {

    @Test
    fun `id is incremented`() {
        val generator = InMemoryIncrementalCustomerOrderIdGenerator()
        val orderId1 = generator.generate()
        val orderId2 = generator.generate()
        orderId1.value shouldBe orderId2.value - 1
    }
}