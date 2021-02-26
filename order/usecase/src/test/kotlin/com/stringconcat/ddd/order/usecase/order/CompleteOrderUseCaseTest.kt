package com.stringconcat.ddd.order.usecase.order

import com.stringconcat.ddd.order.domain.order.CustomerOrderCompletedDomainEvent
import com.stringconcat.ddd.order.usecase.TestCustomerOrderExtractor
import com.stringconcat.ddd.order.usecase.TestCustomerOrderPersister
import com.stringconcat.ddd.order.usecase.orderNotReadyForComplete
import com.stringconcat.ddd.order.usecase.orderReadyForComplete
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class CompleteOrderUseCaseTest {

    private val order = orderReadyForComplete()
    private val extractor = TestCustomerOrderExtractor().apply {
        this[order.id] = order
    }
    private val persister = TestCustomerOrderPersister()

    @Test
    fun `successfully completed`() {
        val useCase = CompleteOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = order.id.value)

        result.shouldBeRight()

        val customerOrder = persister[order.id]
        customerOrder.shouldNotBeNull()
        customerOrder.popEvents() shouldContainExactly listOf(CustomerOrderCompletedDomainEvent(order.id))
    }

    @Test
    fun `invalid state`() {

        val order = orderNotReadyForComplete()
        extractor[order.id] = order

        val useCase = CompleteOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = order.id.value)
        result shouldBeLeft CompleteOrderUseCaseError.InvalidOrderState
    }

    @Test
    fun `order not found`() {
        extractor.clear()
        val useCase = CompleteOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = order.id.value)
        result shouldBeLeft CompleteOrderUseCaseError.OrderNotFound
    }
}