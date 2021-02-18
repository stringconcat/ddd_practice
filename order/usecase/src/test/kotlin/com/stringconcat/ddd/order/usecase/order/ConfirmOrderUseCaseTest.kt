package com.stringconcat.ddd.order.usecase.order

import com.stringconcat.ddd.order.domain.order.CustomerOrderHasBeenConfirmedEvent
import com.stringconcat.ddd.order.domain.order.OrderState
import com.stringconcat.ddd.order.usecase.menu.TestCustomerOrderExtractor
import com.stringconcat.ddd.order.usecase.menu.TestCustomerOrderPersister
import com.stringconcat.ddd.order.usecase.menu.order
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldNotContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test


internal class ConfirmOrderUseCaseTest {

    private val order = order(state = OrderState.WAITING_FOR_PAYMENT)
    private val extractor = TestCustomerOrderExtractor().apply {
        this[order.id] = order
    }
    private val persister = TestCustomerOrderPersister()

    @Test
    fun `successfully confirmed`() {
        val useCase = ConfirmOrderUseCase(extractor, persister)
        val result = useCase.confirmOrder(orderId = order.id.value)

        result.shouldBeRight()

        val customerOrder = persister[order.id]
        customerOrder.shouldNotBeNull()
        customerOrder.popEvents() shouldNotContainExactly listOf(CustomerOrderHasBeenConfirmedEvent(order.id))
    }

    @Test
    fun `invalid state`() {

        val order = order(state = OrderState.PAID)
        extractor[order.id] = order

        val useCase = ConfirmOrderUseCase(extractor, persister)
        val result = useCase.confirmOrder(orderId = order.id.value)
        result shouldBeLeft ConfirmOrderUseCaseError.InvalidOrderState
    }
}