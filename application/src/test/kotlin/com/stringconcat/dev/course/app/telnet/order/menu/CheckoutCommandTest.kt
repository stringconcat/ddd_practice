package com.stringconcat.dev.course.app.telnet.order.menu

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.order.usecase.order.Checkout
import com.stringconcat.ddd.order.usecase.order.CheckoutRequest
import com.stringconcat.ddd.order.usecase.order.CheckoutUseCaseError
import com.stringconcat.ddd.order.usecase.order.PaymentInfo
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.math.BigDecimal
import java.net.URL
import java.util.UUID

class CheckoutCommandTest {

    @Test
    fun `successfully checked out`() {
        val orderId = 2L
        val price = BigDecimal.TEN
        val url = URL("http://localhost")

        val street = "street"
        val building = 123
        val customerId = "bbb7054f-af8e-47da-b32d-5fa0fec0fcf9"

        val paymentInfo = PaymentInfo(orderId = orderId, price = price, paymentURL = url)
        val checkout = TestCheckout(paymentInfo.right())
        val command = CheckoutCommand(checkout)

        val result = command.execute(
            line = "checkout $street $building",
            sessionParameters = emptyMap(),
            sessionId = UUID.fromString(customerId)
        )

        checkout.request.customerId shouldBe customerId
        checkout.request.address.street shouldBe street
        checkout.request.address.building shouldBe building

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
    }

    @ParameterizedTest
    @MethodSource("responses")
    fun `checkout failed`(response: CheckoutUseCaseError) {
        val checkout = TestCheckout(response.left())
        val command = CheckoutCommand(checkout)
        val result = command.execute(
            line = "checkout 134513 45134",
            sessionParameters = emptyMap(),
            sessionId = UUID.randomUUID()
        )
        result shouldBe response.message
    }

    class TestCheckout(private val response: Either<CheckoutUseCaseError, PaymentInfo>) : Checkout {

        lateinit var request: CheckoutRequest

        override fun execute(request: CheckoutRequest): Either<CheckoutUseCaseError, PaymentInfo> {
            this.request = request
            return response
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