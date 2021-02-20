package com.stringconcat.ddd.kitchen.usecase.order

import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderCreatedDomainEvent
import com.stringconcat.ddd.kitchen.domain.order.OrderItem
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class CreateOrderHandlerTest {

    private val persister = TestKitchenOrderPersister()
    private val extractor = TestKitchenOrderExtractor()

    @Test
    fun `order doesn't exist - order created successfully`() {
        val handler = CreateOrderHandler(extractor, persister)

        val orderId = orderId()
        val meal = meal()
        val count = count()

        val itemData = CreateOrderRequest.OrderItemData(
            mealName = meal.value,
            count = count.value
        )

        val request = CreateOrderRequest(
            id = orderId.value,
            items = listOf(itemData)
        )

        val result = handler.createOrder(request)
        result.shouldBeRight()

        val order = persister[orderId]
        order.shouldNotBeNull()
        order.id shouldBe orderId
        order.orderItems shouldContainExactly listOf(OrderItem(meal, count))
        order.popEvents() shouldContainExactly listOf(KitchenOrderCreatedDomainEvent(orderId))
    }

    @Test
    fun `order exists - order not created`() {

        val existingOrder = order()

        val extractor = TestKitchenOrderExtractor().apply {
            this[existingOrder.id] = existingOrder
        }

        val handler = CreateOrderHandler(extractor, persister)

        val request = CreateOrderRequest(
            id = existingOrder.id.value,
            items = emptyList()
        )

        val result = handler.createOrder(request)
        result.shouldBeRight()

        val order = persister[existingOrder.id]
        order.shouldBeNull()
    }

    @Test
    fun `order is empty`() {
        val handler = CreateOrderHandler(extractor, persister)

        val orderId = orderId()

        val request = CreateOrderRequest(
            id = orderId.value,
            items = emptyList()
        )

        val result = handler.createOrder(request)
        result shouldBeLeft CreateOrderUseCaseError.EmptyOrder
    }

    @Test
    fun `invalid count`() {
        val handler = CreateOrderHandler(extractor, persister)

        val orderId = orderId()
        val meal = meal()

        val itemData = CreateOrderRequest.OrderItemData(
            mealName = meal.value,
            count = -1
        )

        val request = CreateOrderRequest(
            id = orderId.value,
            items = listOf(itemData)
        )

        val result = handler.createOrder(request)
        result shouldBeLeft CreateOrderUseCaseError.InvalidCount("Negative value")
    }

    @Test
    fun `invalid meal`() {
        val handler = CreateOrderHandler(extractor, persister)

        val orderId = orderId()
        val count = count()

        val itemData = CreateOrderRequest.OrderItemData(
            mealName = "",
            count = count.value
        )

        val request = CreateOrderRequest(
            id = orderId.value,
            items = listOf(itemData)
        )

        val result = handler.createOrder(request)
        result shouldBeLeft CreateOrderUseCaseError.InvalidMealName("Meal name is empty")
    }
}