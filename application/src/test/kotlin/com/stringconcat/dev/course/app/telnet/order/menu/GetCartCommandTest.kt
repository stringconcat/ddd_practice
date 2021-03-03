package com.stringconcat.dev.course.app.telnet.order.menu

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.usecase.cart.CartInfo
import com.stringconcat.ddd.order.usecase.cart.CartItem
import com.stringconcat.ddd.order.usecase.cart.GetCart
import com.stringconcat.ddd.order.usecase.cart.GetCartUseCaseError
import com.stringconcat.dev.course.app.count
import com.stringconcat.dev.course.app.customerId
import com.stringconcat.dev.course.app.mealId
import com.stringconcat.dev.course.app.mealName
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.UUID

internal class GetCartCommandTest {

    @Test
    fun `cart successfully received`() {

        val item = CartItem(mealId = mealId(1L), mealName = mealName("Pizza"), count(2))

        val customerId = CustomerId("bbb7054f-af8e-47da-b32d-5fa0fec0fcf9")
        val useCase = TestGetCart(CartInfo(customerId, listOf(item)).right())
        val command = GetCartCommand(useCase)

        val result = command.execute(
            line = "",
            sessionParameters = emptyMap(),
            sessionId = UUID.fromString(customerId.value)
        )

        result shouldBe "Cart for customer [bbb7054f-af8e-47da-b32d-5fa0fec0fcf9] \n" +
                "╔═════════╤═══════╤═══════╗\n" +
                "║ Meal id │ Name  │ Count ║\n" +
                "╠═════════╪═══════╪═══════╣\n" +
                "║       1 │ Pizza │     2 ║\n" +
                "╚═════════╧═══════╧═══════╝"

        useCase.customerId shouldBe customerId
    }

    @Test
    fun `cart not found`() {

        val useCase = TestGetCart(GetCartUseCaseError.CartNotFound.left())
        val command = GetCartCommand(useCase)

        val customerId = customerId()

        val result = command.execute(
            line = "",
            sessionParameters = emptyMap(),
            sessionId = UUID.fromString(customerId.value)
        )

        result shouldBe "Cart not found"
        useCase.customerId shouldBe customerId
    }

    class TestGetCart(private val response: Either<GetCartUseCaseError, CartInfo>) : GetCart {

        lateinit var customerId: CustomerId

        override fun execute(forCustomer: CustomerId): Either<GetCartUseCaseError, CartInfo> {
            customerId = forCustomer
            return response
        }
    }
}