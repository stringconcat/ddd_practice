package com.stringconcat.ddd.shop.telnet.cart

import arrow.core.flatMap
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.telnet.ApplicationTelnetCommand
import com.stringconcat.ddd.shop.telnet.LongParameterExtractor
import com.stringconcat.ddd.shop.usecase.cart.RemoveMealFromCart
import java.util.UUID

class RemoveMealFromCartCommand(private val useCase: RemoveMealFromCart) : ApplicationTelnetCommand() {

    override fun execute(line: String, sessionParameters: Map<String, Any>, sessionId: UUID): String {
        return LongParameterExtractor.extract(line)
            .flatMap { mealId ->
                useCase.execute(
                    CustomerId(sessionId.toString()), MealId(mealId)
                ).mapLeft { it.message }
            }
            .map { "OK" }
            .fold(ifLeft = { it }, ifRight = { it })
    }

    override fun getNames() = arrayOf("remove")

    override fun getDescription() = "Remove meal from cart. Usage: remove <meal id>"
}