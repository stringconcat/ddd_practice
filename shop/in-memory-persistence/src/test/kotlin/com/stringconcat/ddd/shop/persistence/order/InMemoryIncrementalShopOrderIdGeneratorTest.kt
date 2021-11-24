package com.stringconcat.ddd.shop.persistence.order

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class InMemoryIncrementalShopOrderIdGeneratorTest {

    @Test
    fun `id is incremented`() {
        val generator = InMemoryIncrementalShopOrderIdGenerator()
        val orderId1 = generator.generate()
        val orderId2 = generator.generate()
        orderId1.toLongValue() shouldBe orderId2.toLongValue() - 1
    }
}