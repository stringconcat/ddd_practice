package com.stringconcat.dev.course.app.listeners

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.order.CustomerOrderCreatedDomainEvent
import com.stringconcat.ddd.order.usecase.cart.RemoveCart
import com.stringconcat.ddd.order.usecase.cart.RemoveCartHandlerError
import com.stringconcat.dev.course.app.customerId
import com.stringconcat.dev.course.app.orderId
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class RemoveCartAfterCheckoutRuleTest {

    @Test
    fun `cart successfully removed`() {

        val customerId = customerId()
        val orderId = orderId()

        val useCase = TestRemoveCart(Unit.right())
        val rule = RemoveCartAfterCheckoutRule(useCase)
        val event = CustomerOrderCreatedDomainEvent(orderId = orderId, forCustomer = customerId)
        rule.handle(event)

        useCase.forCustomer shouldBe customerId
    }

    @Test
    fun `cart removed with error`() {
        val customerId = customerId()
        val orderId = orderId()

        val useCase = TestRemoveCart(RemoveCartHandlerError.CartNotFound.left())
        val rule = RemoveCartAfterCheckoutRule(useCase)
        val event = CustomerOrderCreatedDomainEvent(orderId = orderId, forCustomer = customerId)
        rule.handle(event)

        useCase.forCustomer shouldBe customerId
    }

    class TestRemoveCart(private val response: Either<RemoveCartHandlerError, Unit>) : RemoveCart {

        lateinit var forCustomer: CustomerId

        override fun execute(forCustomer: CustomerId): Either<RemoveCartHandlerError, Unit> {
            this.forCustomer = forCustomer
            return response
        }
    }
}