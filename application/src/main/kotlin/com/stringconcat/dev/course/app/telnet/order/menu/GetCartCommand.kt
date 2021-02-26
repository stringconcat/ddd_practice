package com.stringconcat.dev.course.app.telnet.order.menu

import com.github.freva.asciitable.AsciiTable
import com.github.freva.asciitable.Column
import com.stringconcat.ddd.order.usecase.cart.GetCart
import com.stringconcat.dev.course.app.telnet.ApplicationTelnetCommand
import java.util.UUID

class GetCartCommand(private val useCase: GetCart) : ApplicationTelnetCommand() {

    override fun execute(line: String, sessionParameters: Map<String, Any>, sessionId: UUID): String {
        return useCase.execute(sessionId.toString())
            .fold(ifLeft = { it.message },
                ifRight = { ci ->
                    "Cart for customer [${ci.customerId}] \n" +
                            AsciiTable.getTable(
                                AsciiTable.FANCY_ASCII, ci.items, listOf(
                                    Column().header("Meal id").with { item -> item.mealId.toString() },
                                    Column().header("Name").with { item -> item.mealName },
                                    Column().header("Count").with { item -> item.count.toString() },
                                )
                            )
                })
    }

    override fun getNames() = arrayOf("cart")

    override fun getDescription() = "Show cart"
}