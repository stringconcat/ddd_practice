package com.stringconcat.ddd.shop.usecase.order

import com.stringconcat.ddd.shop.domain.order.CustomerOrderConfirmedDomainEvent
import com.stringconcat.ddd.shop.usecase.TestCustomerOrderExtractor
import com.stringconcat.ddd.shop.usecase.TestCustomerOrderPersister
import com.stringconcat.ddd.shop.usecase.orderId
import com.stringconcat.ddd.shop.usecase.orderNotReadyForConfirm
import com.stringconcat.ddd.shop.usecase.orderReadyForConfirm
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class ConfirmOrderUseCaseTest {

    @Test
    fun `successfully confirmed`() {

        val order = orderReadyForConfirm()
        val extractor = TestCustomerOrderExtractor().apply {
            this[order.id] = order
        }
        val persister = TestCustomerOrderPersister()

        val useCase = ConfirmOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = order.id)

        result.shouldBeRight()

        val customerOrder = persister[order.id]
        customerOrder.shouldNotBeNull()
        customerOrder.popEvents() shouldContainExactly listOf(CustomerOrderConfirmedDomainEvent(order.id))
    }

    @Test
    fun `invalid state`() {

        val order = orderNotReadyForConfirm()
        val extractor = TestCustomerOrderExtractor().apply {
            this[order.id] = order
        }
        val persister = TestCustomerOrderPersister()

        val useCase = ConfirmOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = order.id)
        result shouldBeLeft ConfirmOrderUseCaseError.InvalidOrderState
    }

    @Test
    fun `order not found`() {

        val extractor = TestCustomerOrderExtractor()
        val persister = TestCustomerOrderPersister()

        val useCase = ConfirmOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = orderId())
        result shouldBeLeft ConfirmOrderUseCaseError.OrderNotFound
    }
}