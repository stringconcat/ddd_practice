package com.stringconcat.ddd.shop.usecase.order.scenarios

import com.stringconcat.ddd.shop.domain.orderId
import com.stringconcat.ddd.shop.usecase.TestShopOrderExtractor
import com.stringconcat.ddd.shop.usecase.MockShopOrderPersister
import com.stringconcat.ddd.shop.usecase.order.ConfirmOrderUseCaseError
import com.stringconcat.ddd.shop.usecase.orderNotReadyForConfirm
import com.stringconcat.ddd.shop.usecase.orderReadyForConfirm
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import org.junit.jupiter.api.Test

internal class ConfirmOrderUseCaseTest {

    @Test
    fun `successfully confirmed`() {

        val order = orderReadyForConfirm()
        val extractor = TestShopOrderExtractor().apply {
            this[order.id] = order
        }
        val persister = MockShopOrderPersister()

        val useCase = ConfirmOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = order.id)

        result.shouldBeRight()

        persister.verifyInvoked(order)
        persister.verifyEventsAfterConfirmation(order.id)
    }

    @Test
    fun `invalid state`() {

        val order = orderNotReadyForConfirm()
        val extractor = TestShopOrderExtractor().apply {
            this[order.id] = order
        }
        val persister = MockShopOrderPersister()

        val useCase = ConfirmOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = order.id)

        persister.verifyEmpty()
        result shouldBeLeft ConfirmOrderUseCaseError.InvalidOrderState
    }

    @Test
    fun `order not found`() {

        val extractor = TestShopOrderExtractor()
        val persister = MockShopOrderPersister()

        val useCase = ConfirmOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = orderId())

        persister.verifyEmpty()
        result shouldBeLeft ConfirmOrderUseCaseError.OrderNotFound
    }
}