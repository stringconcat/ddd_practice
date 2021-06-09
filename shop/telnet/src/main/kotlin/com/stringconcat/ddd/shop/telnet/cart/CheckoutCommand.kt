package com.stringconcat.ddd.shop.telnet.cart

import arrow.core.flatMap
import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.common.types.common.CreateAddressError
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.telnet.ApplicationTelnetCommand
import com.stringconcat.ddd.shop.usecase.order.Checkout
import com.stringconcat.ddd.shop.usecase.order.CheckoutRequest
import com.stringconcat.ddd.shop.usecase.order.CheckoutUseCaseError
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

       return Address.from(street = split[1], building = split[2].toInt())
            .map { address -> CheckoutRequest(CustomerId(sessionId.toString()), address) }
            .flatMap { request -> useCase.execute(request) }
            .fold(
                ifRight = { "Please follow this URL for payment ${it.paymentURL}" },
                ifLeft = { err ->
                    when (err) {
                        is CreateAddressError -> "Please provide correct address"
                        is CheckoutUseCaseError -> err.message
                        else -> "something went wrong"
                } }
            )
    }

    override fun getNames() = arrayOf("checkout")

    override fun getDescription() = "Create order. Usage: checkout <street> <building>"
}