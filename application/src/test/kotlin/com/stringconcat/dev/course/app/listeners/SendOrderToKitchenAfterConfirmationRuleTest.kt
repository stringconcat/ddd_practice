package com.stringconcat.dev.course.app.listeners

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.kitchen.usecase.order.CreateOrder
import com.stringconcat.ddd.kitchen.usecase.order.CreateOrderRequest
import com.stringconcat.ddd.kitchen.usecase.order.CreateOrderUseCaseError
import com.stringconcat.ddd.shop.domain.order.ShopOrderConfirmedDomainEvent
import com.stringconcat.ddd.shop.domain.order.OrderItem
import com.stringconcat.dev.course.app.TestShopOrderExtractor
import com.stringconcat.dev.course.app.TestMealExtractor
import com.stringconcat.dev.course.app.count
import com.stringconcat.dev.course.app.shopOrder
import com.stringconcat.dev.course.app.meal
import com.stringconcat.dev.course.app.orderId
import com.stringconcat.dev.course.app.price
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.lang.IllegalStateException

class SendOrderToKitchenAfterConfirmationRuleTest {

    @Test
    fun `order successfully sent`() {

        val meal = meal()
        val price = price()
        val count = count()
        val order = shopOrder(orderItems = setOf(OrderItem(meal.id, price, count)))

        val orderExtractor = TestShopOrderExtractor().apply {
            this[order.id] = order
        }

        val mealExtractor = TestMealExtractor().apply {
            this[meal.id] = meal
        }

        val useCase = TestCreateOrder(Unit.right())

        val rule = SendOrderToKitchenAfterConfirmationRule(
            mealExtractor = mealExtractor,
            shopOrderExtractor = orderExtractor,
            createOrder = useCase
        )

        val event = ShopOrderConfirmedDomainEvent(order.id)
        rule.handle(event)

        useCase.request.id shouldBe order.id.value
        useCase.request.items shouldContainExactly listOf(
            CreateOrderRequest.OrderItemData(
                meal.name.value,
                count.value
            )
        )
    }

    @Test
    fun `order not found`() {
        val orderExtractor = TestShopOrderExtractor()

        val mealExtractor = TestMealExtractor()

        val useCase = TestCreateOrder(Unit.right())

        val rule = SendOrderToKitchenAfterConfirmationRule(
            mealExtractor = mealExtractor,
            shopOrderExtractor = orderExtractor,
            createOrder = useCase
        )

        val event = ShopOrderConfirmedDomainEvent(orderId())

        shouldThrow<IllegalStateException> {
            rule.handle(event)
        }

        useCase.verifyZeroInteraction()
    }

    @Test
    fun `meal not found`() {

        val meal = meal()
        val price = price()
        val count = count()
        val order = shopOrder(orderItems = setOf(OrderItem(meal.id, price, count)))

        val orderExtractor = TestShopOrderExtractor().apply {
            this[order.id] = order
        }

        val mealExtractor = TestMealExtractor()

        val useCase = TestCreateOrder(Unit.right())

        val rule = SendOrderToKitchenAfterConfirmationRule(
            mealExtractor = mealExtractor,
            shopOrderExtractor = orderExtractor,
            createOrder = useCase
        )

        val event = ShopOrderConfirmedDomainEvent(order.id)

        shouldThrow<IllegalStateException> {
            rule.handle(event)
        }

        useCase.verifyZeroInteraction()
    }

    @Test
    fun `order creation error`() {

        val meal = meal()
        val price = price()
        val count = count()
        val order = shopOrder(orderItems = setOf(OrderItem(meal.id, price, count)))

        val orderExtractor = TestShopOrderExtractor().apply {
            this[order.id] = order
        }

        val mealExtractor = TestMealExtractor().apply {
            this[meal.id] = meal
        }

        val useCase = TestCreateOrder(CreateOrderUseCaseError.EmptyOrder.left())

        val rule = SendOrderToKitchenAfterConfirmationRule(
            mealExtractor = mealExtractor,
            shopOrderExtractor = orderExtractor,
            createOrder = useCase
        )

        val event = ShopOrderConfirmedDomainEvent(order.id)

        shouldThrow<IllegalStateException> {
            rule.handle(event)
        }

        useCase.request.id shouldBe order.id.value
        useCase.request.items shouldContainExactly listOf(
            CreateOrderRequest.OrderItemData(
                meal.name.value,
                count.value
            )
        )
    }

    private class TestCreateOrder(val response: Either<CreateOrderUseCaseError, Unit>) : CreateOrder {

        lateinit var request: CreateOrderRequest

        override fun execute(request: CreateOrderRequest): Either<CreateOrderUseCaseError, Unit> {
            this.request = request
            return response
        }

        fun verifyZeroInteraction() {
            ::request.isInitialized.shouldBeFalse()
        }
    }
}