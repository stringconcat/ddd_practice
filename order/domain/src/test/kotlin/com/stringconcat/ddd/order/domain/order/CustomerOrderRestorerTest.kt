package com.stringconcat.ddd.order.domain.order

import com.stringconcat.ddd.order.domain.address
import com.stringconcat.ddd.order.domain.customerId
import com.stringconcat.ddd.order.domain.orderId
import com.stringconcat.ddd.order.domain.orderItem
import com.stringconcat.ddd.order.domain.version
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

internal class CustomerOrderRestorerTest {

    @Test
    fun `restore order - success`() {
        val id = orderId()
        val created = OffsetDateTime.now()
        val customerId = customerId()
        val item = orderItem()
        val items = setOf(item)
        val state = OrderState.COMPLETED
        val version = version()
        val address = address()

        val order = CustomerOrderRestorer.restoreOrder(
            id = id,
            created = created,
            customerId = customerId,
            address = address,
            orderItems = items,
            state = state,
            version = version
        )

        order.id shouldBe id
        order.created shouldBe created
        order.customerId shouldBe customerId
        order.address shouldBe address
        order.orderItems.size shouldBe 1
        val orderItem = order.orderItems.first()
        orderItem.price shouldBe item.price
        orderItem.mealId shouldBe item.mealId
        orderItem.count shouldBe item.count

        order.state shouldBe state
        order.version shouldBe version
        order.popEvents().shouldBeEmpty()
    }
}