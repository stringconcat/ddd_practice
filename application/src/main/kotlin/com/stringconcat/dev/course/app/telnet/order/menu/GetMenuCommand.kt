package com.stringconcat.dev.course.app.telnet.order.menu

import com.github.freva.asciitable.AsciiTable
import com.github.freva.asciitable.Column
import com.khubla.telnet.nvt.NVT
import com.khubla.telnet.shell.command.TelnetCommand
import com.stringconcat.ddd.order.usecase.menu.GetMenuUseCase
import java.util.HashMap

class GetMenuCommand(private val useCase: GetMenuUseCase) : TelnetCommand {

    override fun execute(nvt: NVT, line: String, sessionParameters: HashMap<String, Any>): Boolean {
        val menu = useCase.getMenu()

        val table = AsciiTable.getTable(
            AsciiTable.FANCY_ASCII, menu, listOf(
                Column().with { meal -> meal.id.toString() },
                Column().header("Name").with { meal -> meal.name },
                Column().header("Description").with { meal -> meal.description },
                Column().header("Price").with { meal -> meal.price.toPlainString() },
            )
        )
        nvt.writeln(table.replace("\n", "\r\n"))
        return true
    }

    override fun getNames() = arrayOf("menu")

    override fun getDescription() = "Show menu"
}