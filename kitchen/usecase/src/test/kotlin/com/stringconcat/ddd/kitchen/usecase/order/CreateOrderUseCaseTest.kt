package com.stringconcat.ddd.kitchen.usecase.order

import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderHasBeenCreatedEvent
import com.stringconcat.ddd.kitchen.domain.order.OrderItem
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class CreateOrderUseCaseTest {

    val persister = TestKitchenOrderPersister()

    @Test
    fun `order created successfully`() {
        val useCase = CreateOrderUseCase(persister)

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

        val result = useCase.createOrder(request)
        result.shouldBeRight()

        val order = persister[orderId]
        order.shouldNotBeNull()
        order.id shouldBe orderId
        order.orderItems shouldContainExactly listOf(OrderItem(meal, count))
        order.popEvents() shouldContainExactly listOf(KitchenOrderHasBeenCreatedEvent(orderId))
    }

    @Test
    fun `order is empty`() {
        val useCase = CreateOrderUseCase(persister)

        val orderId = orderId()

        val request = CreateOrderRequest(
            id = orderId.value,
            items = emptyList()
        )

        val result = useCase.createOrder(request)
        result shouldBeLeft CreateOrderUseCaseError.EmptyOrder
    }

    @Test
    fun `invalid count`() {
        val useCase = CreateOrderUseCase(persister)

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

        val result = useCase.createOrder(request)
        result shouldBeLeft CreateOrderUseCaseError.InvalidCount("Negative value")
    }

    @Test
    fun `invalid meal`() {
        val useCase = CreateOrderUseCase(persister)

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

        val result = useCase.createOrder(request)
        result shouldBeLeft CreateOrderUseCaseError.InvalidMealName("Meal name is empty")
    }
}