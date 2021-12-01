package com.stringconcat.ddd.shop.telnet.cart

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.shop.domain.customerId
import com.stringconcat.ddd.shop.domain.orderId
import com.stringconcat.ddd.shop.domain.price
import com.stringconcat.ddd.shop.usecase.order.Checkout
import com.stringconcat.ddd.shop.usecase.order.CheckoutRequest
import com.stringconcat.ddd.shop.usecase.order.CheckoutUseCaseError
import com.stringconcat.ddd.shop.usecase.order.PaymentInfo
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import java.net.URL
import java.util.UUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class CheckoutCommandTest {

    @Test
    fun `successfully checked out`() {
        val orderId = orderId()
        val price = price()
        val url = URL("http://localhost")

        val street = "street"
        val building = 123
        val customerId = customerId()

        val paymentInfo = PaymentInfo(orderId = orderId, price = price, paymentURL = url)
        val checkout = TestCheckout(paymentInfo.right())
        val command = CheckoutCommand(checkout)

        val result = command.execute(
            line = "checkout $street $building",
            sessionParameters = emptyMap(),
            sessionId = UUID.fromString(customerId.toStringValue())
        )

        checkout.request.forCustomer shouldBe customerId
        checkout.request.deliveryTo.streetToStringValue() shouldBe street
        checkout.request.deliveryTo.buildingToIntValue() shouldBe building

        result shouldBe "Please follow this URL for payment $url"
    }

    @Test
    fun `invalid arguments`() {
        val checkout = TestCheckout(CheckoutUseCaseError.CartNotFound.left())
        val command = CheckoutCommand(checkout)
        val result = command.execute(
            line = "checkout 13451345134",
            sessionParameters = emptyMap(),
            sessionId = UUID.randomUUID()
        )
        result shouldBe "Invalid arguments"
        checkout.verifyZeroInteractions()
    }

    @ParameterizedTest
    @MethodSource("responses")
    fun `checkout failed`(response: CheckoutUseCaseError) {

        val street = "street"
        val building = 123
        val customerId = customerId()

        val checkout = TestCheckout(response.left())
        val command = CheckoutCommand(checkout)
        val result = command.execute(
            line = "checkout $street $building",
            sessionParameters = emptyMap(),
            sessionId = UUID.fromString(customerId.toStringValue())
        )

        result shouldBe response.message

        checkout.request.forCustomer shouldBe customerId
        checkout.request.deliveryTo.streetToStringValue() shouldBe street
        checkout.request.deliveryTo.buildingToIntValue() shouldBe building
    }

    class TestCheckout(private val response: Either<CheckoutUseCaseError, PaymentInfo>) : Checkout {

        lateinit var request: CheckoutRequest

        override fun execute(request: CheckoutRequest): Either<CheckoutUseCaseError, PaymentInfo> {
            this.request = request
            return response
        }

        fun verifyZeroInteractions() {
            ::request.isInitialized.shouldBeFalse()
        }
    }

    companion object {

        @JvmStatic
        @Suppress("unused")
        fun responses(): List<Arguments> {
            return listOf(
                Arguments.of(CheckoutUseCaseError.AlreadyHasActiveOrder),
                Arguments.of(CheckoutUseCaseError.CartNotFound),
                Arguments.of(CheckoutUseCaseError.EmptyCart),
                Arguments.of(CheckoutUseCaseError.InvalidAddress("Ooops")),
            )
        }
    }
}