package com.stringconcat.ddd.shop.persistence.cart

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class InMemoryIncrementalCartIdGeneratorTest {

    @Test
    fun `id is incremented`() {
        val generator = InMemoryIncrementalCartIdGenerator()
        val cartId1 = generator.generate()
        val cartId2 = generator.generate()
        cartId1.toLongValue() shouldBe cartId2.toLongValue() - 1
    }
}