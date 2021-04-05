package com.stringconcat.ddd.delivery.domain

import com.stringconcat.ddd.delivery.address
import com.stringconcat.ddd.delivery.orderId
import com.stringconcat.ddd.delivery.orderItem
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class DeliveryOrderTest {

    @Test
    fun `should create delivery order`() {
        val address = address()
        val id = orderId()
        val orderItem = orderItem()
        val result = DeliveryOrder.create(
            id = id,
            deliveryAddress = address,
            orderItems = listOf(orderItem)
        )

        result.shouldBeRight { order ->
            order.id shouldBe id
            order.version.value shouldBe 0
            order.deliveryAddress shouldBe address
            order.orderItems.size shouldBe 1
            val item = order.orderItems.first()
            item.meal shouldBe orderItem.meal
            item.count shouldBe orderItem.count

            order.popEvents() shouldContainExactly listOf(DeliveryOrderCreatedDomainEvent(id))
        }
    }

    @Test
    fun `should fail on no items in the order`() {
        val address = address()
        val id = orderId()
        val result = DeliveryOrder.create(
            id = id,
            deliveryAddress = address,
            orderItems = emptyList()
        )

        result.shouldBeLeft(OrderWithNoItems)
    }
}
