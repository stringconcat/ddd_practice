package com.stringconcat.ddd.kitchen.usecase.order.scenarios

import com.stringconcat.ddd.common.types.count
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderCreatedDomainEvent
import com.stringconcat.ddd.kitchen.domain.order.OrderItem
import com.stringconcat.ddd.kitchen.domain.order.meal
import com.stringconcat.ddd.kitchen.domain.order.order
import com.stringconcat.ddd.kitchen.domain.order.orderId
import com.stringconcat.ddd.kitchen.usecase.order.CreateOrderRequest
import com.stringconcat.ddd.kitchen.usecase.order.CreateOrderUseCaseError
import com.stringconcat.ddd.kitchen.usecase.order.TestKitchenOrderExtractor
import com.stringconcat.ddd.kitchen.usecase.order.TestKitchenOrderPersister
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class CreateOrderHandlerTest {

    @Test
    fun `order doesn't exist - order created successfully`() {

        val persister = TestKitchenOrderPersister()
        val extractor = TestKitchenOrderExtractor()

        val handler = CreateOrderHandler(extractor, persister)

        val orderId = orderId()
        val meal = meal()
        val count = count()

        val itemData = CreateOrderRequest.OrderItemData(
            mealName = meal.toStringValue(),
            count = count.toIntValue()
        )

        val request = CreateOrderRequest(
            id = orderId.toLongValue(),
            items = listOf(itemData)
        )

        val result = handler.execute(request)
        result.shouldBeRight()

        val order = persister[orderId]
        order.shouldNotBeNull()
        order.id shouldBe orderId
        order.meals shouldContainExactly listOf(OrderItem(meal, count))
        order.popEvents() shouldContainExactly listOf(KitchenOrderCreatedDomainEvent(orderId))
    }

    @Test
    fun `order exists - order not created`() {

        val existingOrder = order()

        val persister = TestKitchenOrderPersister()
        val extractor = TestKitchenOrderExtractor().apply {
            this[existingOrder.id] = existingOrder
        }

        val handler = CreateOrderHandler(extractor, persister)

        val request = CreateOrderRequest(
            id = existingOrder.id.toLongValue(),
            items = emptyList()
        )

        val result = handler.execute(request)
        result.shouldBeRight()

        val order = persister[existingOrder.id]
        order.shouldBeNull()
    }

    @Test
    fun `order is empty`() {

        val persister = TestKitchenOrderPersister()
        val extractor = TestKitchenOrderExtractor()
        val handler = CreateOrderHandler(extractor, persister)

        val orderId = orderId()

        val request = CreateOrderRequest(
            id = orderId.toLongValue(),
            items = emptyList()
        )

        val result = handler.execute(request)
        result shouldBeLeft CreateOrderUseCaseError.EmptyOrder
    }

    @Test
    fun `invalid count`() {

        val persister = TestKitchenOrderPersister()
        val extractor = TestKitchenOrderExtractor()
        val handler = CreateOrderHandler(extractor, persister)

        val orderId = orderId()
        val meal = meal()

        val itemData = CreateOrderRequest.OrderItemData(
            mealName = meal.toStringValue(),
            count = -1
        )

        val request = CreateOrderRequest(
            id = orderId.toLongValue(),
            items = listOf(itemData)
        )

        val result = handler.execute(request)
        result shouldBeLeft CreateOrderUseCaseError.InvalidCount("Negative value")
    }

    @Test
    fun `invalid meal`() {

        val persister = TestKitchenOrderPersister()
        val extractor = TestKitchenOrderExtractor()
        val handler = CreateOrderHandler(extractor, persister)

        val orderId = orderId()
        val count = count()

        val itemData = CreateOrderRequest.OrderItemData(
            mealName = "",
            count = count.toIntValue()
        )

        val request = CreateOrderRequest(
            id = orderId.toLongValue(),
            items = listOf(itemData)
        )

        val result = handler.execute(request)
        result shouldBeLeft CreateOrderUseCaseError.InvalidMealName("Meal name is empty")
    }
}