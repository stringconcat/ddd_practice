package com.stringconcat.ddd.order.usecase.order

import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.domain.menu.Price
import com.stringconcat.ddd.order.domain.order.CustomerOrderId
import com.stringconcat.ddd.order.domain.order.CustomerOrderIdGenerator
import com.stringconcat.ddd.order.domain.providers.MealPriceProvider
import com.stringconcat.ddd.order.usecase.TestCartExtractor
import com.stringconcat.ddd.order.usecase.TestCustomerHasActiveOrderRule
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

    private val meal = meal()
    private val address = address()
    private val count = count()
    private val customerId = customerId()
    private val cart = cart(meals = mapOf(meal.id to count), customerId = customerId)

    private val cartExtractor = TestCartExtractor().apply {
        this[cart.customerId] = cart
    }

    private val activeOrderRule = TestCustomerHasActiveOrderRule(false)
    private val orderPersister = TestCustomerOrderPersister()

    object TestCustomerOrderIdGenerator : CustomerOrderIdGenerator {
        val id = orderId()
        override fun generate() = id
    }

    object TestMealPriceProvider : MealPriceProvider {
        val price = price()
        override fun price(mealId: MealId) = price
    }

    object TestPaymentUrlProvider : PaymentUrlProvider {
        val paymentUrl = URL("http://localhost")
        override fun provideUrl(orderId: CustomerOrderId, price: Price) = paymentUrl
    }

    @Test
    fun `order created successfully`() {

        val useCase = CheckoutUseCase(
            idGenerator = TestCustomerOrderIdGenerator,
            cartExtractor = cartExtractor,
            activeOrderRule = activeOrderRule,
            priceProvider = TestMealPriceProvider,
            paymentUrlProvider = TestPaymentUrlProvider,
            customerOrderPersister = orderPersister
        )

        val checkoutRequest = checkoutRequest()
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
        customerOrder.customerId shouldBe customerId
        customerOrder.orderItems.shouldHaveSize(1)
        val orderItem = customerOrder.orderItems.first()
        orderItem.mealId shouldBe meal.id
        orderItem.count shouldBe count
        orderItem.price shouldBe TestMealPriceProvider.price
    }

    @Test
    fun `cart not found`() {
        cartExtractor.clear()
        val useCase = CheckoutUseCase(
            idGenerator = TestCustomerOrderIdGenerator,
            cartExtractor = cartExtractor,
            activeOrderRule = activeOrderRule,
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

        val cart = cart(customerId = customerId)
        cartExtractor[customerId] = cart
        val useCase = CheckoutUseCase(
            idGenerator = TestCustomerOrderIdGenerator,
            cartExtractor = cartExtractor,
            activeOrderRule = activeOrderRule,
            priceProvider = TestMealPriceProvider,
            paymentUrlProvider = TestPaymentUrlProvider,
            customerOrderPersister = orderPersister
        )

        val checkoutRequest = checkoutRequest()
        val result = useCase.execute(checkoutRequest)
        result shouldBeLeft CheckoutUseCaseError.EmptyCart
    }

    @Test
    fun `invalid address - invalid street`() {

        val useCase = CheckoutUseCase(
            idGenerator = TestCustomerOrderIdGenerator,
            cartExtractor = cartExtractor,
            activeOrderRule = activeOrderRule,
            priceProvider = TestMealPriceProvider,
            paymentUrlProvider = TestPaymentUrlProvider,
            customerOrderPersister = orderPersister
        )

        val address = CheckoutRequest.Address("", address.building)
        val checkoutRequest = CheckoutRequest(customerId.value, address)

        val result = useCase.execute(checkoutRequest)
        result shouldBeLeft CheckoutUseCaseError.InvalidAddress("Empty street")
    }

    @Test
    fun `invalid address - invalid building`() {
        val useCase = CheckoutUseCase(
            idGenerator = TestCustomerOrderIdGenerator,
            cartExtractor = cartExtractor,
            activeOrderRule = activeOrderRule,
            priceProvider = TestMealPriceProvider,
            paymentUrlProvider = TestPaymentUrlProvider,
            customerOrderPersister = orderPersister
        )

        val address = CheckoutRequest.Address(address.street, -1)
        val checkoutRequest = CheckoutRequest(customerId.value, address)

        val result = useCase.execute(checkoutRequest)
        result shouldBeLeft CheckoutUseCaseError.InvalidAddress("Negative value")
    }

    @Test
    fun `already has active order`() {

        val activeOrderRule = TestCustomerHasActiveOrderRule(true)

        val useCase = CheckoutUseCase(
            idGenerator = TestCustomerOrderIdGenerator,
            cartExtractor = cartExtractor,
            activeOrderRule = activeOrderRule,
            priceProvider = TestMealPriceProvider,
            paymentUrlProvider = TestPaymentUrlProvider,
            customerOrderPersister = orderPersister
        )

        val result = useCase.execute(checkoutRequest())
        result shouldBeLeft CheckoutUseCaseError.AlreadyHasActiveOrder
    }

    private fun checkoutRequest(): CheckoutRequest {
        val address = CheckoutRequest.Address(address.street, address.building)
        return CheckoutRequest(customerId.value, address)
    }
}