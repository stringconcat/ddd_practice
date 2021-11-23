package com.stringconcat.ddd.shop.usecase.order.scenarios

import com.stringconcat.ddd.shop.domain.orderId
import com.stringconcat.ddd.shop.usecase.TestShopOrderExtractor
import com.stringconcat.ddd.shop.usecase.MockShopOrderPersister
import com.stringconcat.ddd.shop.usecase.order.CompleteOrderUseCaseError
import com.stringconcat.ddd.shop.usecase.orderNotReadyForComplete
import com.stringconcat.ddd.shop.usecase.orderReadyForComplete
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import org.junit.jupiter.api.Test

internal class CompleteOrderUseCaseTest {

    @Test
    fun `successfully completed`() {

        val order = orderReadyForComplete()
        val extractor = TestShopOrderExtractor().apply {
            this[order.id] = order
        }
        val persister = MockShopOrderPersister()

        val useCase = CompleteOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = order.id)

        result.shouldBeRight()

        persister.verifyInvoked(order)
        persister.verifyEventsAfterCompletion(order.id)
    }

    @Test
    fun `invalid state`() {

        val order = orderNotReadyForComplete()
        val extractor = TestShopOrderExtractor().apply {
            this[order.id] = order
        }
        val persister = MockShopOrderPersister()

        val useCase = CompleteOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = order.id)

        persister.verifyEmpty()
        result shouldBeLeft CompleteOrderUseCaseError.InvalidOrderState
    }

    @Test
    fun `order not found`() {
        val extractor = TestShopOrderExtractor()
        val persister = MockShopOrderPersister()

        val useCase = CompleteOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = orderId())

        persister.verifyEmpty()
        result shouldBeLeft CompleteOrderUseCaseError.OrderNotFound
    }
}