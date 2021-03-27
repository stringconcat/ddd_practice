package com.stringconcat.ddd.order.usecase.order

import com.stringconcat.ddd.order.domain.order.CustomerOrderHasBeenPaidDomainEvent
import com.stringconcat.ddd.order.usecase.TestCustomerOrderExtractor
import com.stringconcat.ddd.order.usecase.TestCustomerOrderPersister
import com.stringconcat.ddd.order.usecase.orderId
import com.stringconcat.ddd.order.usecase.orderNotReadyForPay
import com.stringconcat.ddd.order.usecase.orderReadyForPay
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class PayOrderHandlerTest {

    @Test
    fun `successfully payed`() {

        val order = orderReadyForPay()
        val extractor = TestCustomerOrderExtractor().apply {
            this[order.id] = order
        }
        val persister = TestCustomerOrderPersister()

        val handler = PayOrderHandler(extractor, persister)
        val result = handler.execute(orderId = order.id)

        result.shouldBeRight()

        val customerOrder = persister[order.id]
        customerOrder.shouldNotBeNull()
        customerOrder.popEvents() shouldContainExactly listOf(CustomerOrderHasBeenPaidDomainEvent(order.id))
    }

    @Test
    fun `invalid state`() {

        val order = orderNotReadyForPay()
        val extractor = TestCustomerOrderExtractor().apply {
            this[order.id] = order
        }
        val persister = TestCustomerOrderPersister()

        val handler = PayOrderHandler(extractor, persister)
        val result = handler.execute(orderId = order.id)
        result shouldBeLeft PayOrderHandlerError.InvalidOrderState
    }

    @Test
    fun `order not found`() {
        val extractor = TestCustomerOrderExtractor()
        val persister = TestCustomerOrderPersister()

        val handler = PayOrderHandler(extractor, persister)
        val result = handler.execute(orderId = orderId())
        result shouldBeLeft PayOrderHandlerError.OrderNotFound
    }
}