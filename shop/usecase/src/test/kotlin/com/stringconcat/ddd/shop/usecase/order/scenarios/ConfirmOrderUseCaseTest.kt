package com.stringconcat.ddd.shop.usecase.order.scenarios

import com.stringconcat.ddd.shop.domain.orderId
import com.stringconcat.ddd.shop.usecase.MockShopOrderExtractor
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
        val extractor = MockShopOrderExtractor(order)
        val persister = MockShopOrderPersister()

        val useCase = ConfirmOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = order.id)

        result.shouldBeRight()

        persister.verifyInvoked(order)
        persister.verifyEventsAfterConfirmation(order.id)
        extractor.verifyInvokedGetById(order.id)
    }

    @Test
    fun `invalid state`() {

        val order = orderNotReadyForConfirm()
        val extractor = MockShopOrderExtractor(order)
        val persister = MockShopOrderPersister()

        val useCase = ConfirmOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = order.id)

        persister.verifyEmpty()
        extractor.verifyInvokedGetById(order.id)
        result shouldBeLeft ConfirmOrderUseCaseError.InvalidOrderState
    }

    @Test
    fun `order not found`() {

        val extractor = MockShopOrderExtractor()
        val persister = MockShopOrderPersister()

        val useCase = ConfirmOrderUseCase(extractor, persister)

        val orderId = orderId()
        val result = useCase.execute(orderId = orderId)

        persister.verifyEmpty()
        extractor.verifyInvokedGetById(orderId)
        result shouldBeLeft ConfirmOrderUseCaseError.OrderNotFound
    }
}