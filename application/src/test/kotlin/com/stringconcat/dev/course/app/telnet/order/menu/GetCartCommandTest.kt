package com.stringconcat.dev.course.app.telnet.order.menu

import arrow.core.Either
import arrow.core.right
import com.stringconcat.ddd.order.usecase.cart.CartInfo
import com.stringconcat.ddd.order.usecase.cart.CartItem
import com.stringconcat.ddd.order.usecase.cart.GetCart
import com.stringconcat.ddd.order.usecase.cart.GetCartUseCaseError
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.UUID

internal class GetCartCommandTest {

    @Test
    fun `cart successfully received`() {
        val command = GetCartCommand(CartExists)
        val customerId = UUID.fromString("bbb7054f-af8e-47da-b32d-5fa0fec0fcf9")
        val result = command.execute("", emptyMap(), customerId)

        result shouldBe "Cart for customer [bbb7054f-af8e-47da-b32d-5fa0fec0fcf9] \n" +
                "╔═════════╤═══════╤═══════╗\n" +
                "║ Meal id │ Name  │ Count ║\n" +
                "╠═════════╪═══════╪═══════╣\n" +
                "║       1 │ Pizza │     2 ║\n" +
                "╚═════════╧═══════╧═══════╝"
    }

    @Test
    fun `cart not found`() {
        val command = GetCartCommand(CartDoesntExist)
        val result = command.execute("", emptyMap(), UUID.randomUUID())
        result shouldBe "Cart not found"
    }

    object CartDoesntExist : GetCart {
        override fun execute(forCustomer: String) = Either.left(GetCartUseCaseError.CartNotFound)
    }

    object CartExists : GetCart {

        override fun execute(forCustomer: String): Either<GetCartUseCaseError, CartInfo> {
            val item = CartItem(mealId = 1L, mealName = "Pizza", count = 2)
            return CartInfo(forCustomer, listOf(item)).right()
        }
    }
}