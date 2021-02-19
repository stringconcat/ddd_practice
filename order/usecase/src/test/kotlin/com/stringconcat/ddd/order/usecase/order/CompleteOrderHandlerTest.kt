package com.stringconcat.ddd.order.usecase.order

import com.stringconcat.ddd.order.domain.order.CustomerOrderHasBeenCompletedEvent
import com.stringconcat.ddd.order.usecase.menu.TestCustomerOrderExtractor
import com.stringconcat.ddd.order.usecase.menu.TestCustomerOrderPersister
import com.stringconcat.ddd.order.usecase.menu.orderNotReadyForComplete
import com.stringconcat.ddd.order.usecase.menu.orderReadyForComplete
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class CompleteOrderHandlerTest {

    private val order = orderReadyForComplete()
    private val extractor = TestCustomerOrderExtractor().apply {
        this[order.id] = order
    }
    private val persister = TestCustomerOrderPersister()

    @Test
    fun `successfully completed`() {
        val useCase = CompleteOrderHandler(extractor, persister)
        val result = useCase.completeOrder(orderId = order.id.value)

        result.shouldBeRight()

        val customerOrder = persister[order.id]
        customerOrder.shouldNotBeNull()
        customerOrder.popEvents() shouldContainExactly listOf(CustomerOrderHasBeenCompletedEvent(order.id))
    }

    @Test
    fun `invalid state`() {

        val order = orderNotReadyForComplete()
        extractor[order.id] = order

        val useCase = CompleteOrderHandler(extractor, persister)
        val result = useCase.completeOrder(orderId = order.id.value)
        result shouldBeLeft CompleteOrderHandlerError.InvalidOrderState
    }

    @Test
    fun `order not found`() {
        extractor.clear()
        val useCase = CompleteOrderHandler(extractor, persister)
        val result = useCase.completeOrder(orderId = order.id.value)
        result shouldBeLeft CompleteOrderHandlerError.OrderNotFound
    }
}