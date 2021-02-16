package com.stringconcat.ddd.order.domain.order

import com.stringconcat.ddd.order.domain.customerId
import com.stringconcat.ddd.order.domain.orderId
import com.stringconcat.ddd.order.domain.orderItem
import com.stringconcat.ddd.order.domain.version
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

internal class CustomerOrderRestorerTest {

    @Test
    fun `restore order - success`() {
        val id = orderId()
        val created = OffsetDateTime.now()
        val customerId = customerId()
        val orderItems = setOf(orderItem())
        val state = OrderState.COMPLETED
        val version = version()

        val order = CustomerOrderRestorer.restoreOrder(
            id = id,
            created = created,
            customerId = customerId,
            orderItems = orderItems,
            state = state,
            version = version
        )

        order.id shouldBe id
        order.created shouldBe created
        order.customerId shouldBe customerId
        order.orderItems shouldContainExactly orderItems
        order.state shouldBe state
        order.version shouldBe version
        order.popEvents() shouldContainExactly emptyList()
    }
}