package com.stringconcat.ddd.shop.telnet.menu

import arrow.core.flatMap
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.usecase.cart.AddMealToCart
import java.util.UUID

class AddMealToCartCommand(private val useCase: AddMealToCart) : ApplicationTelnetCommand() {

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

    override fun getNames() = arrayOf("add")

    override fun getDescription() = "Add meal to cart. Usage: add <meal id>"
}