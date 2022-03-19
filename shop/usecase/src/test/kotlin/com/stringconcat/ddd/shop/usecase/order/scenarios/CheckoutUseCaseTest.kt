package com.stringconcat.ddd.shop.usecase.order.scenarios

import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.common.types.count
import com.stringconcat.ddd.shop.domain.address
import com.stringconcat.ddd.shop.domain.cart
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.customerId
import com.stringconcat.ddd.shop.domain.meal
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.domain.order.ShopOrderIdGenerator
import com.stringconcat.ddd.shop.domain.orderId
import com.stringconcat.ddd.shop.domain.price
import com.stringconcat.ddd.shop.usecase.MockCartExtractor
import com.stringconcat.ddd.shop.usecase.MockCustomerHasActiveOrder
import com.stringconcat.ddd.shop.usecase.MockShopOrderPersister
import com.stringconcat.ddd.shop.usecase.order.CheckoutRequest
import com.stringconcat.ddd.shop.usecase.order.CheckoutUseCaseError
import com.stringconcat.ddd.shop.usecase.order.providers.PaymentUrlProvider
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.should
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

        val cartExtractor = MockCartExtractor(cart)

        val activeOrderRule = MockCustomerHasActiveOrder(false)
        val orderPersister = MockShopOrderPersister()

        val price = price()

        val useCase = CheckoutUseCase(
            idGenerator = TestShopOrderIdGenerator,
            cartExtractor = cartExtractor,
            activeOrder = activeOrderRule,
            getMealPrice = { price },
            paymentUrlProvider = TestPaymentUrlProvider,
            shopOrderPersister = orderPersister
        )

        val checkoutRequest = checkoutRequest(address, customerId)
        val result = useCase.execute(checkoutRequest)

        val orderId = TestShopOrderIdGenerator.id

        activeOrderRule.verifyInvoked(cart.forCustomer)
        cartExtractor.verifyInvoked(cart.forCustomer)
        orderPersister.verifyInvoked(
            orderId, address, customerId,
            meal.id, count, price
        )
        result.shouldBeRight().should {
            it.orderId shouldBe orderId
            it.paymentURL shouldBe TestPaymentUrlProvider.paymentUrl
            orderPersister.verifyPrice(it.price)
        }
    }

    @Test
    fun `cart not found`() {

        val activeOrderRule = MockCustomerHasActiveOrder(false)
        val orderPersister = MockShopOrderPersister()

        val cartExtractor = MockCartExtractor()
        val useCase = CheckoutUseCase(
            idGenerator = TestShopOrderIdGenerator,
            cartExtractor = cartExtractor,
            activeOrder = activeOrderRule,
            getMealPrice = { price() },
            paymentUrlProvider = TestPaymentUrlProvider,
            shopOrderPersister = orderPersister
        )

        val checkoutRequest = checkoutRequest()
        val result = useCase.execute(checkoutRequest)

        orderPersister.verifyEmpty()
        activeOrderRule.verifyEmpty()
        cartExtractor.verifyInvoked(checkoutRequest.forCustomer)
        result shouldBeLeft CheckoutUseCaseError.CartNotFound
    }

    @Test
    fun `cart is empty`() {

        val customerId = customerId()

        val cart = cart(customerId = customerId)
        val activeOrderRule = MockCustomerHasActiveOrder(false)
        val orderPersister = MockShopOrderPersister()

        val cartExtractor = MockCartExtractor(cart)

        val useCase = CheckoutUseCase(
            idGenerator = TestShopOrderIdGenerator,
            cartExtractor = cartExtractor,
            activeOrder = activeOrderRule,
            getMealPrice = { price() },
            paymentUrlProvider = TestPaymentUrlProvider,
            shopOrderPersister = orderPersister
        )

        val checkoutRequest = checkoutRequest(customerId = customerId)
        val result = useCase.execute(checkoutRequest)

        orderPersister.verifyEmpty()
        activeOrderRule.verifyInvoked(checkoutRequest.forCustomer)
        cartExtractor.verifyInvoked(checkoutRequest.forCustomer)
        result shouldBeLeft CheckoutUseCaseError.EmptyCart
    }

    @Test
    fun `already has active order`() {

        val cart = cart()
        val cartExtractor = MockCartExtractor(cart)
        val activeOrderRule = MockCustomerHasActiveOrder(true)
        val orderPersister = MockShopOrderPersister()

        val useCase = CheckoutUseCase(
            idGenerator = TestShopOrderIdGenerator,
            cartExtractor = cartExtractor,
            activeOrder = activeOrderRule,
            getMealPrice = { price() },
            paymentUrlProvider = TestPaymentUrlProvider,
            shopOrderPersister = orderPersister
        )

        orderPersister.verifyEmpty()
        cartExtractor.verifyEmpty()
        activeOrderRule.verifyEmpty()
        useCase
            .execute(checkoutRequest(customerId = cart.forCustomer))
            .shouldBeLeft(CheckoutUseCaseError.AlreadyHasActiveOrder)
    }

    private fun checkoutRequest(
        address: Address = address(),
        customerId: CustomerId = customerId()
    ): CheckoutRequest {
        return Address
            .from(address.streetToStringValue(), address.buildingToIntValue())
            .map { CheckoutRequest(customerId, address) }
            .fold(
                ifLeft = { throw IllegalStateException() },
                ifRight = { it }
            )
    }

    object TestShopOrderIdGenerator : ShopOrderIdGenerator {
        val id = orderId()
        override fun generate() = id
    }

    object TestPaymentUrlProvider : PaymentUrlProvider {
        val paymentUrl = URL("http://localhost")
        override fun provideUrl(orderId: ShopOrderId, price: Price) = paymentUrl
    }
}