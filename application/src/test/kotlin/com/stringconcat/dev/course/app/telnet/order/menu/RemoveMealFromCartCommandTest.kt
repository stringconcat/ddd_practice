package com.stringconcat.dev.course.app.telnet.order.menu

import arrow.core.Either
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.usecase.cart.RemoveMealFromCart
import com.stringconcat.ddd.order.usecase.cart.RemoveMealFromCartUseCaseError
import com.stringconcat.dev.course.app.mealId
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.UUID

class RemoveMealFromCartCommandTest {

    @Test
    fun `meal successfully removed`() {
        val command = RemoveMealFromCartCommand(TestRemoveMealFromCart)
        val result = command.execute(
            line = "remove ${TestRemoveMealFromCart.mealId.value}",
            sessionParameters = emptyMap(),
            sessionId = UUID.fromString(TestRemoveMealFromCart.customerId.value)
        )

        result shouldBe "OK"
    }

    @Test
    fun `cart not found`() {
        val command = RemoveMealFromCartCommand(TestRemoveMealFromCart)
        val result = command.execute(
            line = "remove 14",
            sessionParameters = emptyMap(),
            sessionId = UUID.fromString(TestRemoveMealFromCart.customerId.value)
        )

        result shouldBe "Cart not found"
    }

    @Test
    fun `invalid parameter`() {
        val command = RemoveMealFromCartCommand(TestRemoveMealFromCart)
        val result = command.execute(
            line = "remove fff",
            sessionParameters = emptyMap(),
            sessionId = UUID.randomUUID()
        )

        result shouldBe "Invalid argument"
    }

    object TestRemoveMealFromCart : RemoveMealFromCart {

        val mealId = mealId(3L)
        val customerId = CustomerId("bbb7054f-af8e-47da-b32d-5fa0fec0fcf9")

        override fun execute(forCustomer: CustomerId, mealId: MealId): Either<RemoveMealFromCartUseCaseError, Unit> {
            return if (forCustomer == this.customerId && mealId == this.mealId) {
                Either.right(Unit)
            } else {
                Either.left(RemoveMealFromCartUseCaseError.CartNotFound)
            }
        }
    }
}