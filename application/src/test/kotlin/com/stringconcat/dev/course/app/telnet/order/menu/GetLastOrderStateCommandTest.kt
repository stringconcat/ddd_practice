package com.stringconcat.dev.course.app.telnet.order.menu

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.order.OrderState
import com.stringconcat.ddd.order.usecase.order.GetLastOrderState
import com.stringconcat.ddd.order.usecase.order.GetLastOrderStateUseCaseError
import com.stringconcat.dev.course.app.customerId
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.util.UUID

internal class GetLastOrderStateCommandTest {

    @ParameterizedTest
    @EnumSource(OrderState::class)
    fun `last state successfully received`(state: OrderState) {

        val useCase = TestGetLastOrderState(state.right())
        val command = GetLastOrderStateCommand(useCase)
        val customerId = customerId()

        val result = command.execute(
            line = "",
            sessionParameters = emptyMap(),
            sessionId = UUID.fromString(customerId.value)
        )

        result shouldBe state.label()
        useCase.customerId shouldBe customerId
    }

    @Test
    fun `order not found`() {

        val useCase = TestGetLastOrderState(GetLastOrderStateUseCaseError.OrderNotFound.left())
        val command = GetLastOrderStateCommand(useCase)

        val customerId = customerId()

        val result = command.execute(
            line = "",
            sessionParameters = emptyMap(),
            sessionId = UUID.fromString(customerId.value)
        )

        result shouldBe GetLastOrderStateUseCaseError.OrderNotFound.message
        useCase.customerId shouldBe customerId
    }

    class TestGetLastOrderState(val response: Either<GetLastOrderStateUseCaseError, OrderState>) : GetLastOrderState {
        lateinit var customerId: CustomerId
        override fun execute(forCustomer: CustomerId): Either<GetLastOrderStateUseCaseError, OrderState> {
            this.customerId = forCustomer
            return response
        }
    }
}