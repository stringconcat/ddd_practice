package com.stringconcat.ddd.shop.domain.order

import com.stringconcat.ddd.shop.domain.address
import com.stringconcat.ddd.shop.domain.customerId
import com.stringconcat.ddd.shop.domain.orderId
import com.stringconcat.ddd.shop.domain.orderItem
import com.stringconcat.ddd.shop.domain.version
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

internal class ShopOrderRestorerTest {

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

        val order = ShopOrderRestorer.restoreOrder(
            id = id,
            created = created,
            forCustomer = customerId,
            address = address,
            orderItems = items,
            state = state,
            version = version
        )

        order.id shouldBe id
        order.created shouldBe created
        order.forCustomer shouldBe customerId
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