package com.stringconcat.ddd.shop.telnet.cart

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.common.types.count
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.customerId
import com.stringconcat.ddd.shop.domain.mealId
import com.stringconcat.ddd.shop.domain.mealName
import com.stringconcat.ddd.shop.usecase.cart.CartInfo
import com.stringconcat.ddd.shop.usecase.cart.CartItem
import com.stringconcat.ddd.shop.usecase.cart.GetCart
import com.stringconcat.ddd.shop.usecase.cart.GetCartUseCaseError
import io.kotest.matchers.shouldBe
import java.util.UUID
import org.junit.jupiter.api.Test

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