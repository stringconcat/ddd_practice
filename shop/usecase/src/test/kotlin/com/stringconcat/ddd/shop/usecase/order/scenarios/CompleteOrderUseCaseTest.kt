package com.stringconcat.ddd.shop.usecase.order.scenarios

import com.stringconcat.ddd.shop.domain.order.ShopOrderCompletedDomainEvent
import com.stringconcat.ddd.shop.usecase.TestShopOrderExtractor
import com.stringconcat.ddd.shop.usecase.TestShopOrderPersister
import com.stringconcat.ddd.shop.usecase.order.CompleteOrderUseCaseError
import com.stringconcat.ddd.shop.usecase.orderId
import com.stringconcat.ddd.shop.usecase.orderNotReadyForComplete
import com.stringconcat.ddd.shop.usecase.orderReadyForComplete
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class CompleteOrderUseCaseTest {

    @Test
    fun `successfully completed`() {

        val order = orderReadyForComplete()
        val extractor = TestShopOrderExtractor().apply {
            this[order.id] = order
        }
        val persister = TestShopOrderPersister()

        val useCase = CompleteOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = order.id)

        result.shouldBeRight()

        val shopOrder = persister[order.id]
        shopOrder.shouldNotBeNull()
        shopOrder.popEvents() shouldContainExactly listOf(ShopOrderCompletedDomainEvent(order.id))
    }

    @Test
    fun `invalid state`() {

        val order = orderNotReadyForComplete()
        val extractor = TestShopOrderExtractor().apply {
            this[order.id] = order
        }
        val persister = TestShopOrderPersister()

        val useCase = CompleteOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = order.id)
        result shouldBeLeft CompleteOrderUseCaseError.InvalidOrderState
    }

    @Test
    fun `order not found`() {
        val extractor = TestShopOrderExtractor()
        val persister = TestShopOrderPersister()

        val useCase = CompleteOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = orderId())
        result shouldBeLeft CompleteOrderUseCaseError.OrderNotFound
    }
}