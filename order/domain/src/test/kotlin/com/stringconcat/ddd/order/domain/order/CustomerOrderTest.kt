package com.stringconcat.ddd.order.domain.order

import com.stringconcat.ddd.order.domain.TestCustomerHasActiveOrderRule
import com.stringconcat.ddd.order.domain.TestMealPriceProvider
import com.stringconcat.ddd.order.domain.address
import com.stringconcat.ddd.order.domain.cart
import com.stringconcat.ddd.order.domain.count
import com.stringconcat.ddd.order.domain.mealId
import com.stringconcat.ddd.order.domain.order
import com.stringconcat.ddd.order.domain.orderId
import com.stringconcat.ddd.order.domain.orderItem
import com.stringconcat.ddd.order.domain.price
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.math.BigDecimal

class CustomerOrderTest {

    val id = orderId()

    private val idGenerator = object : CustomerOrderIdGenerator {
        override fun generate() = id
    }

    private val activeOrderRule = TestCustomerHasActiveOrderRule(false)

    private val mealPriceProvider = TestMealPriceProvider()

    @Test
    fun `checkout - success`() {
        val mealId = mealId()
        val count = count()
        val price = price()
        val address = address()
        mealPriceProvider[mealId] = price
        val cart = cart(mapOf(mealId to count))

        val result = CustomerOrder.checkout(
            cart = cart,
            idGenerator = idGenerator,
            activeOrder = activeOrderRule,
            priceProvider = mealPriceProvider,
            address = address
        )

        result shouldBeRight {
            it.customerId shouldBe cart.customerId
            it.orderItems shouldContainExactly listOf(OrderItem(mealId, price, count))
            it.id shouldBe id
            it.address shouldBe address
            it.state shouldBe OrderState.WAITING_FOR_PAYMENT
            it.popEvents() shouldContainExactly listOf(CustomerOrderHasBeenCreatedEvent(id))
        }
    }

    @Test
    fun `checkout - already has active order`() {
        val mealId = mealId()
        val count = count()
        val price = price()
        val address = address()
        mealPriceProvider[mealId] = price
        val cart = cart(mapOf(mealId to count))

        val activeOrderRule = TestCustomerHasActiveOrderRule(true)

        val result = CustomerOrder.checkout(
            cart = cart,
            idGenerator = idGenerator,
            activeOrder = activeOrderRule,
            priceProvider = mealPriceProvider,
            address = address
        )

        result shouldBeLeft CheckoutError.AlreadyHasActiveOrder
    }

    @Test
    fun `checkout - empty cart`() {
        val cart = cart()
        val result = CustomerOrder.checkout(
            cart = cart,
            idGenerator = idGenerator,
            activeOrder = activeOrderRule,
            priceProvider = mealPriceProvider,
            address = address()
        )
        result shouldBeLeft CheckoutError.EmptyCart
    }

    @ParameterizedTest
    @EnumSource(names = ["WAITING_FOR_PAYMENT", "CONFIRMED", "PAID"])
    fun `active - true`(state: OrderState) {
        val order = order(state = state)
        order.isActive() shouldBe true
    }

    @ParameterizedTest
    @EnumSource(names = ["COMPLETED", "CANCELLED"])
    fun `active - false`(state: OrderState) {
        val order = order(state = state)
        order.isActive() shouldBe false
    }

    @Test
    fun `complete order - success`() {
        val order = order(state = OrderState.CONFIRMED)
        order.complete() shouldBeRight Unit
        order.state shouldBe OrderState.COMPLETED
        order.popEvents() shouldContainExactly listOf(CustomerOrderHasBeenCompletedEvent(order.id))
    }

    @Test
    fun `complete order - already`() {
        val order = order(state = OrderState.COMPLETED)
        order.complete() shouldBeRight Unit
        order.state shouldBe OrderState.COMPLETED
        order.popEvents() shouldContainExactly emptyList()
    }

    @ParameterizedTest
    @EnumSource(names = ["WAITING_FOR_PAYMENT", "PAID", "CANCELLED"])
    fun `complete order - invalid state`(state: OrderState) {
        val order = order(state = state)
        order.complete() shouldBeLeft InvalidState
        order.state shouldBe state
        order.popEvents() shouldContainExactly emptyList()
    }

    @Test
    fun `pay order - success`() {
        val order = order(state = OrderState.WAITING_FOR_PAYMENT)
        order.pay() shouldBeRight Unit
        order.state shouldBe OrderState.PAID
        order.popEvents() shouldContainExactly listOf(CustomerOrderHasBeenPaidEvent(order.id))
    }

    @Test
    fun `pay order - already`() {
        val order = order(state = OrderState.PAID)
        order.pay() shouldBeRight Unit
        order.state shouldBe OrderState.PAID
        order.popEvents() shouldContainExactly emptyList()
    }

    @ParameterizedTest
    @EnumSource(names = ["CONFIRMED", "COMPLETED", "CANCELLED"])
    fun `pay - invalid state`(state: OrderState) {
        val order = order(state = state)
        order.pay() shouldBeLeft InvalidState
        order.state shouldBe state
        order.popEvents() shouldContainExactly emptyList()
    }

    @Test
    fun `cancel order - success`() {
        val order = order(state = OrderState.PAID)
        order.cancel() shouldBeRight Unit
        order.state shouldBe OrderState.CANCELLED
        order.popEvents() shouldContainExactly listOf(CustomerOrderHasBeenCancelledEvent(order.id))
    }

    @Test
    fun `cancel order - already`() {
        val order = order(state = OrderState.CANCELLED)
        order.cancel() shouldBeRight Unit
        order.state shouldBe OrderState.CANCELLED
        order.popEvents() shouldContainExactly emptyList()
    }

    @ParameterizedTest
    @EnumSource(names = ["CONFIRMED", "COMPLETED", "WAITING_FOR_PAYMENT"])
    fun `cancel - invalid state`(state: OrderState) {
        val order = order(state = state)
        order.cancel() shouldBeLeft InvalidState
        order.state shouldBe state
        order.popEvents() shouldContainExactly emptyList()
    }

    @Test
    fun `confirm order - success`() {
        val order = order(state = OrderState.PAID)
        order.confirm() shouldBeRight Unit
        order.state shouldBe OrderState.CONFIRMED
        order.popEvents() shouldContainExactly listOf(CustomerOrderHasBeenConfirmedEvent(order.id))
    }

    @Test
    fun `confirm order - already`() {
        val order = order(state = OrderState.CONFIRMED)
        order.confirm() shouldBeRight Unit
        order.state shouldBe OrderState.CONFIRMED
        order.popEvents() shouldContainExactly emptyList()
    }

    @ParameterizedTest
    @EnumSource(names = ["CANCELLED", "COMPLETED", "WAITING_FOR_PAYMENT"])
    fun `confirm - invalid state`(state: OrderState) {
        val order = order(state = state)
        order.confirm() shouldBeLeft InvalidState
        order.state shouldBe state
        order.popEvents() shouldContainExactly emptyList()
    }

    @Test
    fun `calculate total`() {
        val orderItem1 = orderItem(price(BigDecimal("1.03")), count(2))
        val orderItem2 = orderItem(price(BigDecimal("91.33")), count(4))

        val order = order(orderItems = setOf(orderItem1, orderItem2))
        order.totalPrice() shouldBe price(BigDecimal("367.38"))
    }
}