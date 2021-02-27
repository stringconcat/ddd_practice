package com.stringconcat.ddd.kitchen.usecase.order

import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderCookedDomainEvent
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldNotContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class CookOrderUseCaseTest {

    private val order = order()
    private val persister = TestKitchenOrderPersister()
    private val extractor = TestKitchenOrderExtractor().apply {
        this[order.id] = order
    }

    @Test
    fun `successfully complete`() {
        val useCase = CookOrderUseCase(extractor, persister)
        val result = useCase.execute(order.id)
        result.shouldBeRight()

        val savedOrder = persister[order.id]
        savedOrder shouldBe order
        order.popEvents() shouldNotContainExactly listOf(KitchenOrderCookedDomainEvent(order.id))
    }

    @Test
    fun `order not found`() {
        extractor.clear()
        val useCase = CookOrderUseCase(extractor, persister)
        val result = useCase.execute(order.id)
        result shouldBeLeft CookOrderUseCaseError.OrderNotFound
    }
}