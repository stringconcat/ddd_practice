package com.stringconcat.ddd.shop.usecase.order.scenarios

import com.stringconcat.ddd.shop.domain.order.ShopOrderCancelledDomainEvent
import com.stringconcat.ddd.shop.usecase.TestShopOrderExtractor
import com.stringconcat.ddd.shop.usecase.TestShopOrderPersister
import com.stringconcat.ddd.shop.usecase.order.CancelOrderUseCaseError
import com.stringconcat.ddd.shop.usecase.orderId
import com.stringconcat.ddd.shop.usecase.orderNotReadyForCancel
import com.stringconcat.ddd.shop.usecase.orderReadyForCancel
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class CancelOrderUseCaseTest {

    @Test
    fun `successfully confirmed`() {

        val order = orderReadyForCancel()
        val extractor = TestShopOrderExtractor().apply {
            this[order.id] = order
        }
        val persister = TestShopOrderPersister()

        val useCase = CancelOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = order.id)

        result.shouldBeRight()

        val shopOrder = persister[order.id]
        shopOrder.shouldNotBeNull()
        shopOrder.popEvents() shouldContainExactly listOf(ShopOrderCancelledDomainEvent(order.id))
    }

    @Test
    fun `invalid state`() {

        val order = orderNotReadyForCancel()

        val extractor = TestShopOrderExtractor().apply {
            this[order.id] = order
        }
        val persister = TestShopOrderPersister()

        val useCase = CancelOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = order.id)
        result shouldBeLeft CancelOrderUseCaseError.InvalidOrderState
    }

    @Test
    fun `order not found`() {
        val extractor = TestShopOrderExtractor()
        val persister = TestShopOrderPersister()

        val useCase = CancelOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = orderId())
        result shouldBeLeft CancelOrderUseCaseError.OrderNotFound
    }
}