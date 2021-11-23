package com.stringconcat.ddd.shop.persistence.order

import com.stringconcat.ddd.shop.domain.customerId
import com.stringconcat.ddd.shop.domain.order
import com.stringconcat.ddd.shop.domain.order.ShopOrderCompletedDomainEvent
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.domain.orderId
import com.stringconcat.ddd.shop.persistence.TestEventPublisher
import com.stringconcat.ddd.shop.persistence.orderWithEvents
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.junit.jupiter.api.Test

internal class InMemoryShopOrderRepositoryTest {

    @Test
    fun `saving order - order doesn't exist`() {
        val eventPublisher = TestEventPublisher()
        val repository = InMemoryShopOrderRepository(eventPublisher)
        val order = orderWithEvents()

        repository.save(order)

        val storedOrder = repository.storage[order.id]
        storedOrder shouldBeSameInstanceAs order
        eventPublisher.storage.shouldHaveSize(1)

        val event = eventPublisher.storage.first()
        event.shouldBeInstanceOf<ShopOrderCompletedDomainEvent>()
        event.orderId shouldBe order.id
    }

    @Test
    fun `saving order - order exists`() {

        val id = orderId()
        val existingOrder = order(id = id)

        val eventPublisher = TestEventPublisher()
        val repository = InMemoryShopOrderRepository(eventPublisher)
        repository.storage[existingOrder.id] = existingOrder

        val updatedOrder = orderWithEvents(id)
        repository.save(updatedOrder)

        val event = eventPublisher.storage.first()
        event.shouldBeInstanceOf<ShopOrderCompletedDomainEvent>()
        event.orderId shouldBe updatedOrder.id
    }

    @Test
    fun `get by id - order exists`() {
        val existingOrder = order()

        val eventPublisher = TestEventPublisher()
        val repository = InMemoryShopOrderRepository(eventPublisher)
        repository.storage[existingOrder.id] = existingOrder

        val order = repository.getById(existingOrder.id)
        order shouldBeSameInstanceAs existingOrder
    }

    @Test
    fun `get by id - order doesn't exist`() {
        val eventPublisher = TestEventPublisher()
        val repository = InMemoryShopOrderRepository(eventPublisher)
        val order = repository.getById(orderId())
        order.shouldBeNull()
    }

    @Test
    fun `get last - repository is empty`() {
        val eventPublisher = TestEventPublisher()
        val repository = InMemoryShopOrderRepository(eventPublisher)
        val order = repository.getLastOrder(customerId())
        order.shouldBeNull()
    }

    @Test
    fun `get last - success`() {

        val customerId = customerId()
        val firstOrder = order(customerId = customerId)
        val lastOrder = order(customerId = customerId)
        val oneMoreOrder = order(customerId = customerId())

        val eventPublisher = TestEventPublisher()
        val repository = InMemoryShopOrderRepository(eventPublisher)
        repository.save(firstOrder)
        repository.save(lastOrder)
        repository.save(oneMoreOrder)

        val order = repository.getLastOrder(customerId)
        order shouldBeSameInstanceAs lastOrder
    }

    @Test
    fun `get all - storage is empty`() {
        val orderId = orderId()
        val eventPublisher = TestEventPublisher()
        val repository = InMemoryShopOrderRepository(eventPublisher)
        val result = repository.getAll(orderId, 100)
        result.shouldBeEmpty()
    }

    @Test
    fun `get all - limit is less than collection`() {

        val limit = 10
        val collectionSize = 20

        val eventPublisher = TestEventPublisher()
        val repository = InMemoryShopOrderRepository(eventPublisher)

        repeat(collectionSize) {
            val order = order(id = ShopOrderId(it.toLong()))
            repository.storage[order.id] = order
        }

        val result = repository.getAll(ShopOrderId(3), limit)

        result.shouldHaveSize(limit)
        result.first().id shouldBe ShopOrderId(3)
        result.last().id shouldBe ShopOrderId(12)
    }

    @Test
    fun `get all - limit is bigger than collection`() {

        val limit = 10
        val collectionSize = 5

        val eventPublisher = TestEventPublisher()
        val repository = InMemoryShopOrderRepository(eventPublisher)

        repeat(collectionSize) {
            val order = order(id = ShopOrderId(it.toLong()))
            repository.storage[order.id] = order
        }

        val result = repository.getAll(ShopOrderId(0), limit)

        result.shouldHaveSize(collectionSize)
        result.first().id shouldBe ShopOrderId(0)
        result.last().id shouldBe ShopOrderId(4)
    }
}