package com.stringconcat.dev.course.app.telnet.order.menu

import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.order.OrderState
import com.stringconcat.ddd.shop.usecase.order.GetLastOrderState
import com.stringconcat.dev.course.app.telnet.ApplicationTelnetCommand
import java.util.UUID

class GetLastOrderStateCommand(private val useCase: GetLastOrderState) : ApplicationTelnetCommand() {

    override fun execute(line: String, sessionParameters: Map<String, Any>, sessionId: UUID): String {
        return useCase.execute(CustomerId(sessionId.toString()))
            .fold(ifLeft = { it.message },
                ifRight = { it.label() })
    }

    override fun getNames() = arrayOf("state")

    override fun getDescription() = "Get last order state"
}

fun OrderState.label(): String {
    return when (this) {
        OrderState.CANCELLED -> "Cancelled"
        OrderState.COMPLETED -> "Completed"
        OrderState.CONFIRMED -> "Confirmed"
        OrderState.PAID -> "Paid"
        OrderState.WAITING_FOR_PAYMENT -> "Waiting for payment"
    }
}