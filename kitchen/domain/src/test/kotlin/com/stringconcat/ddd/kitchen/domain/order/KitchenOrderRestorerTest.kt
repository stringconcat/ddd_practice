package com.stringconcat.ddd.kitchen.domain.order

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class KitchenOrderRestorerTest {

    @Test
    fun `restore order - success`() {
        val id = orderId()
        val item = orderItem()
        val items = listOf(item)
        val cooked = true
        val version = version()

        val order = KitchenOrderRestorer.restoreOrder(
            id = id,
            orderItems = items,
            cooked = cooked,
            version = version
        )

        order.id shouldBe id
        order.cooked shouldBe cooked
        order.version shouldBe version

        order.meals.size shouldBe 1
        val orderItem = order.meals.first()
        orderItem.count shouldBe item.count

        order.popEvents().shouldBeEmpty()
    }
}