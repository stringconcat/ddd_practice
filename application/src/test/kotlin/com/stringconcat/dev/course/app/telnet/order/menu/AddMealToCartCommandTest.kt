package com.stringconcat.dev.course.app.telnet.order.menu

import arrow.core.Either
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.usecase.cart.AddMealToCart
import com.stringconcat.ddd.order.usecase.cart.AddMealToCartUseCaseError
import com.stringconcat.dev.course.app.mealId
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.UUID

class AddMealToCartCommandTest {

    @Test
    fun `meal successfully added`() {
        val command = AddMealToCartCommand(TestAddMealToCart)
        val result = command.execute(
            line = "add ${TestAddMealToCart.mealId.value}",
            sessionParameters = emptyMap(),
            sessionId = UUID.fromString(TestAddMealToCart.customerId.value)
        )

        result shouldBe "OK"
    }

    @Test
    fun `meal not found`() {
        val command = AddMealToCartCommand(TestAddMealToCart)
        val result = command.execute(
            line = "add 14",
            sessionParameters = emptyMap(),
            sessionId = UUID.fromString(TestAddMealToCart.customerId.value)
        )

        result shouldBe "Meal not found"
    }

    @Test
    fun `invalid parameter`() {
        val command = AddMealToCartCommand(TestAddMealToCart)
        val result = command.execute(
            line = "add gggg",
            sessionParameters = emptyMap(),
            sessionId = UUID.randomUUID()
        )

        result shouldBe "Invalid argument"
    }

    object TestAddMealToCart : AddMealToCart {

        val mealId = mealId()
        val customerId = CustomerId("bbb7054f-af8e-47da-b32d-5fa0fec0fcf9")

        override fun execute(forCustomer: CustomerId, mealId: MealId): Either<AddMealToCartUseCaseError, Unit> {
            return if (forCustomer == this.customerId && mealId == this.mealId) {
                Either.right(Unit)
            } else {
                Either.left(AddMealToCartUseCaseError.MealNotFound)
            }
        }
    }
}