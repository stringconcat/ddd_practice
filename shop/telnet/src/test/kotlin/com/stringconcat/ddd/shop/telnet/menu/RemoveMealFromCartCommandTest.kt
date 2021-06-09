package com.stringconcat.ddd.shop.telnet.menu

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.telnet.cart.RemoveMealFromCartCommand
import com.stringconcat.ddd.shop.telnet.customerId
import com.stringconcat.ddd.shop.telnet.mealId
import com.stringconcat.ddd.shop.usecase.cart.RemoveMealFromCart
import com.stringconcat.ddd.shop.usecase.cart.RemoveMealFromCartUseCaseError
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.UUID

class RemoveMealFromCartCommandTest {

    @Test
    fun `meal successfully removed`() {

        val mealId = mealId()
        val customerId = customerId()

        val useCase = TestRemoveMealFromCart(Unit.right())
        val command = RemoveMealFromCartCommand(useCase)
        val result = command.execute(
            line = "remove ${mealId.value}",
            sessionParameters = emptyMap(),
            sessionId = UUID.fromString(customerId.value)
        )

        result shouldBe "OK"
        useCase.mealId shouldBe mealId
        useCase.customerId shouldBe customerId
    }

    @Test
    fun `cart not found`() {

        val mealId = mealId()
        val customerId = customerId()
        val useCase = TestRemoveMealFromCart(RemoveMealFromCartUseCaseError.CartNotFound.left())

        val command = RemoveMealFromCartCommand(useCase)
        val result = command.execute(
            line = "remove ${mealId.value}",
            sessionParameters = emptyMap(),
            sessionId = UUID.fromString(customerId.value)
        )

        result shouldBe "Cart not found"
        useCase.mealId shouldBe mealId
        useCase.customerId shouldBe customerId
    }

    @Test
    fun `invalid parameter`() {
        val useCase = TestRemoveMealFromCart(Unit.right())
        val command = RemoveMealFromCartCommand(useCase)
        val result = command.execute(
            line = "remove fff",
            sessionParameters = emptyMap(),
            sessionId = UUID.randomUUID()
        )

        result shouldBe "Invalid argument"
        useCase.verifyZeroInteraction()
    }

    class TestRemoveMealFromCart(val response: Either<RemoveMealFromCartUseCaseError, Unit>) : RemoveMealFromCart {
        lateinit var mealId: MealId
        lateinit var customerId: CustomerId

        override fun execute(forCustomer: CustomerId, mealId: MealId): Either<RemoveMealFromCartUseCaseError, Unit> {
            this.customerId = forCustomer
            this.mealId = mealId
            return response
        }

        fun verifyZeroInteraction() {
            ::mealId.isInitialized.shouldBeFalse()
            ::customerId.isInitialized.shouldBeFalse()
        }
    }
}