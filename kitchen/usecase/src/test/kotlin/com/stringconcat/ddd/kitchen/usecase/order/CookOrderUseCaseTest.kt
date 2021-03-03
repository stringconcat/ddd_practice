package com.stringconcat.ddd.kitchen.usecase.order

import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderCookedDomainEvent
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldNotContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class CookOrderUseCaseTest {

    @Test
    fun `successfully complete`() {

        val order = order()
        val persister = TestKitchenOrderPersister()
        val extractor = TestKitchenOrderExtractor().apply {
            this[order.id] = order
        }

        val useCase = CookOrderUseCase(extractor, persister)
        val result = useCase.execute(order.id)
        result.shouldBeRight()

        val savedOrder = persister[order.id]
        savedOrder shouldBe order
        order.popEvents() shouldNotContainExactly listOf(KitchenOrderCookedDomainEvent(order.id))
    }

    @Test
    fun `order not found`() {
        val order = order()
        val persister = TestKitchenOrderPersister()
        val extractor = TestKitchenOrderExtractor()

        val useCase = CookOrderUseCase(extractor, persister)
        val result = useCase.execute(order.id)
        result shouldBeLeft CookOrderUseCaseError.OrderNotFound
    }
}