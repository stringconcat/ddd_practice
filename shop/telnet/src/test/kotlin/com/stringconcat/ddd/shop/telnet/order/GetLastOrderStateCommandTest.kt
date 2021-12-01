package com.stringconcat.ddd.shop.telnet.order

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.customerId
import com.stringconcat.ddd.shop.domain.order.OrderState
import com.stringconcat.ddd.shop.usecase.order.GetLastOrderState
import com.stringconcat.ddd.shop.usecase.order.GetLastOrderStateUseCaseError
import io.kotest.matchers.shouldBe
import java.util.UUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

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
            sessionId = UUID.fromString(customerId.toStringValue())
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
            sessionId = UUID.fromString(customerId.toStringValue())
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