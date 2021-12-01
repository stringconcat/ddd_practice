package com.stringconcat.ddd.shop.telnet.cart

import com.github.freva.asciitable.AsciiTable
import com.github.freva.asciitable.Column
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.telnet.ApplicationTelnetCommand
import com.stringconcat.ddd.shop.usecase.cart.GetCart
import java.util.UUID

class GetCartCommand(private val useCase: GetCart) : ApplicationTelnetCommand() {

    override fun execute(line: String, sessionParameters: Map<String, Any>, sessionId: UUID): String {
        return useCase.execute(
            CustomerId(sessionId.toString())
        )
            .fold(ifLeft = { it.message },
                ifRight = { ci ->
                    "Cart for customer [${ci.forCustomer.toStringValue()}] \n" +
                            AsciiTable.getTable(
                                AsciiTable.FANCY_ASCII, ci.items, listOf(
                                    Column().header("Meal id").with { item ->
                                        item.mealId.toLongValue().toString()
                                    },
                                    Column().header("Name").with { item -> item.mealName.toStringValue() },
                                    Column().header("Count").with { item -> item.count.toIntValue().toString() },
                                )
                            )
                })
    }

    override fun getNames() = arrayOf("cart")

    override fun getDescription() = "Show cart"
}