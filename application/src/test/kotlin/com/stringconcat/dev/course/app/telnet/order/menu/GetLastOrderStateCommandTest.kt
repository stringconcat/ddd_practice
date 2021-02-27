package com.stringconcat.dev.course.app.telnet.order.menu

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.order.OrderState
import com.stringconcat.ddd.order.usecase.order.GetLastOrderState
import com.stringconcat.ddd.order.usecase.order.GetLastOrderStateUseCaseError
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
        val customerId = "bbb7054f-af8e-47da-b32d-5fa0fec0fcf9"

        val result = command.execute(
            line = "",
            sessionParameters = emptyMap(),
            sessionId = UUID.fromString(customerId)
        )

        result shouldBe state.label()
        useCase.customerId.value shouldBe customerId
    }

    @Test
    fun `order not found`() {

        val useCase = TestGetLastOrderState(GetLastOrderStateUseCaseError.OrderNotFound.left())
        val command = GetLastOrderStateCommand(useCase)

        val result = command.execute(
            line = "",
            sessionParameters = emptyMap(),
            sessionId = UUID.randomUUID()
        )

        result shouldBe GetLastOrderStateUseCaseError.OrderNotFound.message
    }

    class TestGetLastOrderState(val response: Either<GetLastOrderStateUseCaseError, OrderState>) : GetLastOrderState {
        lateinit var customerId: CustomerId
        override fun execute(customerId: CustomerId): Either<GetLastOrderStateUseCaseError, OrderState> {
            this.customerId = customerId
            return response
        }
    }
}