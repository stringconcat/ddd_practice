package com.stringconcat.dev.course.app.telnet.order.menu

import arrow.core.flatMap
import com.stringconcat.ddd.order.usecase.cart.AddMealToCartUseCase
import com.stringconcat.dev.course.app.telnet.ApplicationTelnetCommand
import java.util.UUID

class AddMealToCartCommand(private val useCase: AddMealToCartUseCase) : ApplicationTelnetCommand() {

    override fun execute(line: String, sessionParameters: Map<String, Any>, sessionId: UUID): String {
        return LongParameterExtractor.extract(line)
            .flatMap { mealId ->
                useCase.execute(sessionId.toString(), mealId)
                    .mapLeft { it.message }
            }
            .map { "OK" }
            .fold(ifLeft = { it }, ifRight = { it })
    }

    override fun getNames() = arrayOf("add")

    override fun getDescription() = "Add meal to cart"
}