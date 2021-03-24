package com.stringconcat.ddd.shop.domain.cart

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import org.junit.jupiter.api.Test
import java.util.UUID

class CustomerIdTest {

    @Test
    fun `check equality`() {
        val id = UUID.randomUUID().toString()

        val customerId1 = CustomerId(id)
        val customerId2 = CustomerId(id)

        customerId1 shouldBe customerId2
        customerId1 shouldNotBeSameInstanceAs customerId2
        customerId1.value shouldBe customerId2.value
    }
}