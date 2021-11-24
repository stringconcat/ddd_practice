package com.stringconcat.ddd.shop.telnet.menu

import com.github.freva.asciitable.AsciiTable
import com.github.freva.asciitable.Column
import com.stringconcat.ddd.shop.telnet.ApplicationTelnetCommand
import com.stringconcat.ddd.shop.usecase.menu.GetMenu
import java.util.UUID

class GetMenuCommand(private val useCase: GetMenu) : ApplicationTelnetCommand() {

    override fun execute(line: String, sessionParameters: Map<String, Any>, sessionId: UUID): String {
        val menu = useCase.execute()

        return AsciiTable.getTable(
            AsciiTable.FANCY_ASCII, menu, listOf(
                Column().with { meal -> meal.id.toLongValue().toString() },
                Column().header("Name").with { meal -> meal.name.toStringValue() },
                Column().header("Description").with { meal -> meal.description.toStringValue() },
                Column().header("Price").with { meal -> meal.price.toStringValue() },
            )
        )
    }

    override fun getNames() = arrayOf("menu")

    override fun getDescription() = "Show menu"
}