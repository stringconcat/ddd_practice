package com.stringconcat.ddd.shop.usecase.order.scenarios

import com.stringconcat.ddd.shop.domain.order.ShopOrderPaidDomainEvent
import com.stringconcat.ddd.shop.domain.orderId
import com.stringconcat.ddd.shop.usecase.TestShopOrderExtractor
import com.stringconcat.ddd.shop.usecase.TestShopOrderPersister
import com.stringconcat.ddd.shop.usecase.order.PayOrderHandlerError
import com.stringconcat.ddd.shop.usecase.orderNotReadyForPay
import com.stringconcat.ddd.shop.usecase.orderReadyForPay
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class PayOrderHandlerTest {

    @Test
    fun `successfully payed`() {

        val order = orderReadyForPay()
        val extractor = TestShopOrderExtractor().apply {
            this[order.id] = order
        }
        val persister = TestShopOrderPersister()

        val handler = PayOrderHandler(extractor, persister)
        val result = handler.execute(orderId = order.id)

        result.shouldBeRight()

        val customerOrder = persister[order.id]
        customerOrder.shouldNotBeNull()
        customerOrder.popEvents() shouldContainExactly listOf(ShopOrderPaidDomainEvent(order.id))
    }

    @Test
    fun `invalid state`() {

        val order = orderNotReadyForPay()
        val extractor = TestShopOrderExtractor().apply {
            this[order.id] = order
        }
        val persister = TestShopOrderPersister()

        val handler = PayOrderHandler(extractor, persister)
        val result = handler.execute(orderId = order.id)
        result shouldBeLeft PayOrderHandlerError.InvalidOrderState
    }

    @Test
    fun `order not found`() {
        val extractor = TestShopOrderExtractor()
        val persister = TestShopOrderPersister()

        val handler = PayOrderHandler(extractor, persister)
        val result = handler.execute(orderId = orderId())
        result shouldBeLeft PayOrderHandlerError.OrderNotFound
    }
}