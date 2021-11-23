package com.stringconcat.ddd.shop.usecase.order.scenarios

import com.stringconcat.ddd.shop.domain.orderId
import com.stringconcat.ddd.shop.usecase.TestShopOrderExtractor
import com.stringconcat.ddd.shop.usecase.MockShopOrderPersister
import com.stringconcat.ddd.shop.usecase.order.PayOrderHandlerError
import com.stringconcat.ddd.shop.usecase.orderNotReadyForPay
import com.stringconcat.ddd.shop.usecase.orderReadyForPay
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import org.junit.jupiter.api.Test

internal class PayOrderHandlerTest {

    @Test
    fun `successfully payed`() {

        val order = orderReadyForPay()
        val extractor = TestShopOrderExtractor().apply {
            this[order.id] = order
        }
        val persister = MockShopOrderPersister()

        val handler = PayOrderHandler(extractor, persister)
        val result = handler.execute(orderId = order.id)

        result.shouldBeRight()

        persister.verifyInvoked(order)
        persister.verifyEventsAfterPayment(order.id)
    }

    @Test
    fun `invalid state`() {

        val order = orderNotReadyForPay()
        val extractor = TestShopOrderExtractor().apply {
            this[order.id] = order
        }
        val persister = MockShopOrderPersister()

        val handler = PayOrderHandler(extractor, persister)
        val result = handler.execute(orderId = order.id)

        persister.verifyEmpty()
        result shouldBeLeft PayOrderHandlerError.InvalidOrderState
    }

    @Test
    fun `order not found`() {
        val extractor = TestShopOrderExtractor()
        val persister = MockShopOrderPersister()

        val handler = PayOrderHandler(extractor, persister)
        val result = handler.execute(orderId = orderId())

        persister.verifyEmpty()
        result shouldBeLeft PayOrderHandlerError.OrderNotFound
    }
}