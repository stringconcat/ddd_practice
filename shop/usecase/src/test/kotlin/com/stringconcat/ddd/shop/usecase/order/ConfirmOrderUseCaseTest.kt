package com.stringconcat.ddd.shop.usecase.order

import com.stringconcat.ddd.shop.domain.order.ShopOrderConfirmedDomainEvent
import com.stringconcat.ddd.shop.usecase.TestShopOrderExtractor
import com.stringconcat.ddd.shop.usecase.TestShopOrderPersister
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
        val extractor = TestShopOrderExtractor().apply {
            this[order.id] = order
        }
        val persister = TestShopOrderPersister()

        val useCase = ConfirmOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = order.id)

        result.shouldBeRight()

        val shopOrder = persister[order.id]
        shopOrder.shouldNotBeNull()
        shopOrder.popEvents() shouldContainExactly listOf(ShopOrderConfirmedDomainEvent(order.id))
    }

    @Test
    fun `invalid state`() {

        val order = orderNotReadyForConfirm()
        val extractor = TestShopOrderExtractor().apply {
            this[order.id] = order
        }
        val persister = TestShopOrderPersister()

        val useCase = ConfirmOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = order.id)
        result shouldBeLeft ConfirmOrderUseCaseError.InvalidOrderState
    }

    @Test
    fun `order not found`() {

        val extractor = TestShopOrderExtractor()
        val persister = TestShopOrderPersister()

        val useCase = ConfirmOrderUseCase(extractor, persister)
        val result = useCase.execute(orderId = orderId())
        result shouldBeLeft ConfirmOrderUseCaseError.OrderNotFound
    }
}