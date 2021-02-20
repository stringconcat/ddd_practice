package com.stringconcat.ddd.order.usecase.order

import com.stringconcat.ddd.order.domain.order.CustomerOrderHasBeenDomainEvent
import com.stringconcat.ddd.order.usecase.menu.TestCustomerOrderExtractor
import com.stringconcat.ddd.order.usecase.menu.TestCustomerOrderPersister
import com.stringconcat.ddd.order.usecase.menu.orderNotReadyForPay
import com.stringconcat.ddd.order.usecase.menu.orderReadyForPay
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class PayOrderHandlerTest {

    private val order = orderReadyForPay()
    private val extractor = TestCustomerOrderExtractor().apply {
        this[order.id] = order
    }
    private val persister = TestCustomerOrderPersister()

    @Test
    fun `successfully payed`() {
        val handler = PayOrderHandler(extractor, persister)
        val result = handler.payOrder(orderId = order.id.value)

        result.shouldBeRight()

        val customerOrder = persister[order.id]
        customerOrder.shouldNotBeNull()
        customerOrder.popEvents() shouldContainExactly listOf(CustomerOrderHasBeenDomainEvent(order.id))
    }

    @Test
    fun `invalid state`() {

        val order = orderNotReadyForPay()
        extractor[order.id] = order

        val handler = PayOrderHandler(extractor, persister)
        val result = handler.payOrder(orderId = order.id.value)
        result shouldBeLeft PayOrderHandlerError.InvalidOrderState
    }

    @Test
    fun `order not found`() {
        extractor.clear()
        val handler = PayOrderHandler(extractor, persister)
        val result = handler.payOrder(orderId = order.id.value)
        result shouldBeLeft PayOrderHandlerError.OrderNotFound
    }
}