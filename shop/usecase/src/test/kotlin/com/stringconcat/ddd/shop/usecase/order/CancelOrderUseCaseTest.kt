package com.stringconcat.ddd.shop.usecase.order

import com.stringconcat.ddd.shop.domain.order.CustomerOrderCancelledDomainEvent
import com.stringconcat.ddd.shop.usecase.TestCustomerOrderExtractor
import com.stringconcat.ddd.shop.usecase.TestCustomerOrderPersister
import com.stringconcat.ddd.shop.usecase.orderId
import com.stringconcat.ddd.shop.usecase.orderNotReadyForCancel
import com.stringconcat.ddd.shop.usecase.orderReadyForCancel
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class CancelOrderUseCaseTest {

    @Test
    fun `successfully confirmed`() {

        val order = orderReadyForCancel()
        val extractor = TestCustomerOrderExtractor().apply {
            this[order.id] = order
        }
        val persister = TestCustomerOrderPersister()

        val useCase = CancelOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = order.id)

        result.shouldBeRight()

        val customerOrder = persister[order.id]
        customerOrder.shouldNotBeNull()
        customerOrder.popEvents() shouldContainExactly listOf(CustomerOrderCancelledDomainEvent(order.id))
    }

    @Test
    fun `invalid state`() {

        val order = orderNotReadyForCancel()

        val extractor = TestCustomerOrderExtractor().apply {
            this[order.id] = order
        }
        val persister = TestCustomerOrderPersister()

        val useCase = CancelOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = order.id)
        result shouldBeLeft CancelOrderUseCaseError.InvalidOrderState
    }

    @Test
    fun `order not found`() {
        val extractor = TestCustomerOrderExtractor()
        val persister = TestCustomerOrderPersister()

        val useCase = CancelOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = orderId())
        result shouldBeLeft CancelOrderUseCaseError.OrderNotFound
    }
}