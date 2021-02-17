package com.stringconcat.ddd.kitchen.domain.order

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class KitchenOrderTest {

    @Test
    fun `create order - success`() {
        val id = orderId()
        val item = orderItem()
        val items = setOf(item)

        val order = KitchenOrder.create(id = id, items)
        order.id shouldBe id
        order.cooked shouldBe false
        order.orderItems.size shouldBe 1
        val orderItem = order.orderItems.first()
        orderItem.meal shouldBe item.meal
        orderItem.count shouldBe item.count

        order.popEvents() shouldContainExactly listOf(KitchenOrderHasBeenCreatedEvent(id))
    }

    @Test
    fun `order cooked - success`() {
        val order = order(cooked = false)
        order.cooked()
        order.cooked shouldBe true
        order.popEvents() shouldContainExactly listOf(KitchenOrderHasBeenCookedEvent(order.id))
    }

    @Test
    fun `order cooked - already cooked`() {
        val order = order(cooked = true)
        order.cooked()
        order.cooked shouldBe true
        order.popEvents() shouldContainExactly emptyList()
    }
}