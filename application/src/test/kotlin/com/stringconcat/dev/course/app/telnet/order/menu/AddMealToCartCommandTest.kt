package com.stringconcat.dev.course.app.telnet.order.menu

import arrow.core.Either
import com.stringconcat.ddd.order.usecase.cart.AddMealToCart
import com.stringconcat.ddd.order.usecase.cart.AddMealToCartUseCaseError
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.UUID

class AddMealToCartCommandTest {

    @Test
    fun `meal successfully added`() {
        val command = AddMealToCartCommand(TestAddMealToCart)
        val result = command.execute(
            line = "add ${TestAddMealToCart.mealId}",
            sessionParameters = emptyMap(),
            sessionId = UUID.fromString(TestAddMealToCart.customerId)
        )

        result shouldBe "OK"
    }

    @Test
    fun `meal not found`() {
        val command = AddMealToCartCommand(TestAddMealToCart)
        val result = command.execute(
            line = "add 14",
            sessionParameters = emptyMap(),
            sessionId = UUID.fromString(TestAddMealToCart.customerId)
        )

        result shouldBe "Meal not found"
    }

    @Test
    fun `invalid command`() {
        val command = AddMealToCartCommand(TestAddMealToCart)
        val result = command.execute(
            line = "add gggg",
            sessionParameters = emptyMap(),
            sessionId = UUID.randomUUID()
        )

        result shouldBe "Invalid argument"
    }

    object TestAddMealToCart : AddMealToCart {

        const val mealId = 3L
        const val customerId = "bbb7054f-af8e-47da-b32d-5fa0fec0fcf9"

        override fun execute(forCustomer: String, mealId: Long): Either<AddMealToCartUseCaseError, Unit> {
            return if (forCustomer == this.customerId && mealId == this.mealId) {
                Either.right(Unit)
            } else {
                Either.left(AddMealToCartUseCaseError.MealNotFound)
            }
        }
    }

}