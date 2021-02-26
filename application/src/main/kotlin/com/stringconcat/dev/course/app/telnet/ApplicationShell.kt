package com.stringconcat.dev.course.app.telnet

import com.khubla.telnet.nvt.NVT
import com.khubla.telnet.shell.command.CommandOrientedShellImpl
import com.khubla.telnet.shell.command.TelnetCommandRegistry

class ApplicationShell(
    nvt: NVT?,
    val telnetCommandRegistry: TelnetCommandRegistry,
) : CommandOrientedShellImpl(nvt, telnetCommandRegistry, null) {

    private val helloMessage = "Hi Random Netrunner,\r\nand welcome to the restaurant.\r\n\r\nAvailable commands:"
    private val byeMessage = "Be careful"

    override fun onConnect() {
        nvt.writeln(helloMessage)
        telnetCommandRegistry.commands.map {
            val names = it.names.joinToString()
            val description = it.description
            nvt.writeln("$names - $description")
        }
    }

    override fun onDisconnect() {
        nvt.writeln(byeMessage)
    }
}