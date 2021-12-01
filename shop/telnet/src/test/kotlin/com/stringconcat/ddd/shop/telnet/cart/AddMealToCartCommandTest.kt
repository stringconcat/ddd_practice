package com.stringconcat.ddd.shop.telnet.cart

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.customerId
import com.stringconcat.ddd.shop.domain.mealId
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.usecase.cart.AddMealToCart
import com.stringconcat.ddd.shop.usecase.cart.AddMealToCartUseCaseError
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import java.util.UUID
import org.junit.jupiter.api.Test

class AddMealToCartCommandTest {

    @Test
    fun `meal successfully added`() {

        val mealId = mealId()
        val customerId = customerId()

        val useCase = TestAddMealToCart(Unit.right())

        val command = AddMealToCartCommand(useCase)
        val result = command.execute(
            line = "add ${mealId.toLongValue()}",
            sessionParameters = emptyMap(),
            sessionId = UUID.fromString(customerId.toStringValue())
        )

        result shouldBe "OK"
        useCase.mealId shouldBe mealId
        useCase.customerId shouldBe customerId
    }

    @Test
    fun `meal not found`() {

        val mealId = mealId()
        val customerId = customerId()

        val useCase = TestAddMealToCart(AddMealToCartUseCaseError.MealNotFound.left())
        val command = AddMealToCartCommand(useCase)

        val result = command.execute(
            line = "add ${mealId.toLongValue()}",
            sessionParameters = emptyMap(),
            sessionId = UUID.fromString(customerId.toStringValue())
        )

        result shouldBe "Meal not found"
        useCase.mealId shouldBe mealId
        useCase.customerId shouldBe customerId
    }

    @Test
    fun `invalid parameter`() {
        val useCase = TestAddMealToCart(Unit.right())

        val command = AddMealToCartCommand(useCase)
        val result = command.execute(
            line = "add gggg",
            sessionParameters = emptyMap(),
            sessionId = UUID.randomUUID()
        )

        result shouldBe "Invalid argument"
        useCase.verifyZeroInteractions()
    }

    class TestAddMealToCart(private val response: Either<AddMealToCartUseCaseError, Unit>) : AddMealToCart {

        lateinit var mealId: MealId
        lateinit var customerId: CustomerId

        override fun execute(forCustomer: CustomerId, mealId: MealId): Either<AddMealToCartUseCaseError, Unit> {
            this.customerId = forCustomer
            this.mealId = mealId
            return response
        }

        fun verifyZeroInteractions() {
            ::mealId.isInitialized.shouldBeFalse()
            ::customerId.isInitialized.shouldBeFalse()
        }
    }
}