package com.stringconcat.dev.course.app.telnet.order.menu

import com.stringconcat.ddd.order.usecase.order.Checkout
import com.stringconcat.ddd.order.usecase.order.CheckoutRequest
import com.stringconcat.dev.course.app.telnet.ApplicationTelnetCommand
import java.util.UUID

class CheckoutCommand(private val useCase: Checkout) : ApplicationTelnetCommand() {

    companion object {
        const val ARGUMENT_LENGTH = 3
    }

    override fun execute(line: String, sessionParameters: Map<String, Any>, sessionId: UUID): String {
        val split = line.split(" ")

        if (split.size != ARGUMENT_LENGTH || split[2].toIntOrNull() == null) {
            return "Invalid arguments"
        }

        val address = CheckoutRequest.Address(street = split[1], building = split[2].toInt())
        val request = CheckoutRequest(sessionId.toString(), address)

        return useCase.execute(request)
            .fold(
                ifLeft = { it.message },
                ifRight = { "Please follow this URL for payment ${it.paymentURL}" }
            )
    }

    override fun getNames() = arrayOf("checkout")

    override fun getDescription() = "Create order"
}