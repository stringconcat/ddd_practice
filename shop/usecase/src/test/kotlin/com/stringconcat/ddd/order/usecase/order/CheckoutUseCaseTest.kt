package com.stringconcat.ddd.order.usecase.order

import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.domain.menu.Price
import com.stringconcat.ddd.order.domain.order.CustomerOrderId
import com.stringconcat.ddd.order.domain.order.CustomerOrderIdGenerator
import com.stringconcat.ddd.order.domain.order.MealPriceProvider
import com.stringconcat.ddd.order.usecase.TestCartExtractor
import com.stringconcat.ddd.order.usecase.TestCustomerHasActiveOrder
import com.stringconcat.ddd.order.usecase.TestCustomerOrderPersister
import com.stringconcat.ddd.order.usecase.address
import com.stringconcat.ddd.order.usecase.cart
import com.stringconcat.ddd.order.usecase.count
import com.stringconcat.ddd.order.usecase.customerId
import com.stringconcat.ddd.order.usecase.meal
import com.stringconcat.ddd.order.usecase.orderId
import com.stringconcat.ddd.order.usecase.price
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.net.URL

internal class CheckoutUseCaseTest {

    @Test
    fun `order created successfully`() {

        val meal = meal()
        val address = address()
        val count = count()
        val customerId = customerId()
        val cart = cart(meals = mapOf(meal.id to count), customerId = customerId)

        val cartExtractor = TestCartExtractor().apply {
            this[cart.forCustomer] = cart
        }

        val activeOrderRule = TestCustomerHasActiveOrder(false)
        val orderPersister = TestCustomerOrderPersister()

        val useCase = CheckoutUseCase(
            idGenerator = TestCustomerOrderIdGenerator,
            cartExtractor = cartExtractor,
            activeOrder = activeOrderRule,
            priceProvider = TestMealPriceProvider,
            paymentUrlProvider = TestPaymentUrlProvider,
            customerOrderPersister = orderPersister
        )

        val checkoutRequest = checkoutRequest(address, customerId)
        val result = useCase.execute(checkoutRequest)

        val orderId = TestCustomerOrderIdGenerator.id

        val customerOrder = orderPersister[orderId]
        customerOrder.shouldNotBeNull()

        result shouldBeRight {
            it.orderId shouldBe orderId
            it.paymentURL shouldBe TestPaymentUrlProvider.paymentUrl
            it.price shouldBe customerOrder.totalPrice()
        }

        customerOrder.id shouldBe orderId
        customerOrder.address shouldBe address
        customerOrder.forCustomer shouldBe customerId
        customerOrder.orderItems.shouldHaveSize(1)
        val orderItem = customerOrder.orderItems.first()
        orderItem.mealId shouldBe meal.id
        orderItem.count shouldBe count
        orderItem.price shouldBe TestMealPriceProvider.price
    }

    @Test
    fun `cart not found`() {

        val activeOrderRule = TestCustomerHasActiveOrder(false)
        val orderPersister = TestCustomerOrderPersister()

        val cartExtractor = TestCartExtractor()
        val useCase = CheckoutUseCase(
            idGenerator = TestCustomerOrderIdGenerator,
            cartExtractor = cartExtractor,
            activeOrder = activeOrderRule,
            priceProvider = TestMealPriceProvider,
            paymentUrlProvider = TestPaymentUrlProvider,
            customerOrderPersister = orderPersister
        )

        val checkoutRequest = checkoutRequest()
        val result = useCase.execute(checkoutRequest)
        result shouldBeLeft CheckoutUseCaseError.CartNotFound
    }

    @Test
    fun `cart is empty`() {

        val customerId = customerId()

        val cart = cart(customerId = customerId)
        val activeOrderRule = TestCustomerHasActiveOrder(false)
        val orderPersister = TestCustomerOrderPersister()

        val cartExtractor = TestCartExtractor().apply {
            this[customerId] = cart
        }

        val useCase = CheckoutUseCase(
            idGenerator = TestCustomerOrderIdGenerator,
            cartExtractor = cartExtractor,
            activeOrder = activeOrderRule,
            priceProvider = TestMealPriceProvider,
            paymentUrlProvider = TestPaymentUrlProvider,
            customerOrderPersister = orderPersister
        )

        val checkoutRequest = checkoutRequest(customerId = customerId)
        val result = useCase.execute(checkoutRequest)
        result shouldBeLeft CheckoutUseCaseError.EmptyCart
    }

    @Test
    fun `already has active order`() {

        val cart = cart()
        val cartExtractor = TestCartExtractor().apply {
            this[cart.forCustomer] = cart
        }
        val activeOrderRule = TestCustomerHasActiveOrder(true)
        val orderPersister = TestCustomerOrderPersister()

        val useCase = CheckoutUseCase(
            idGenerator = TestCustomerOrderIdGenerator,
            cartExtractor = cartExtractor,
            activeOrder = activeOrderRule,
            priceProvider = TestMealPriceProvider,
            paymentUrlProvider = TestPaymentUrlProvider,
            customerOrderPersister = orderPersister
        )

        useCase
            .execute(checkoutRequest(customerId = cart.forCustomer))
            .shouldBeLeft(CheckoutUseCaseError.AlreadyHasActiveOrder)
    }

    private fun checkoutRequest(
        address: Address = address(),
        customerId: CustomerId = customerId()
    ): CheckoutRequest {
        return Address
            .from(address.street, address.building)
            .map { CheckoutRequest(customerId, address) }
            .fold(
                ifLeft = { throw IllegalStateException() },
                ifRight = { it }
            )
    }

    object TestCustomerOrderIdGenerator : CustomerOrderIdGenerator {
        val id = orderId()
        override fun generate() = id
    }

    object TestMealPriceProvider : MealPriceProvider {
        val price = price()
        override fun getPrice(forMealId: MealId) = price
    }

    object TestPaymentUrlProvider : PaymentUrlProvider {
        val paymentUrl = URL("http://localhost")
        override fun provideUrl(orderId: CustomerOrderId, price: Price) = paymentUrl
    }
}