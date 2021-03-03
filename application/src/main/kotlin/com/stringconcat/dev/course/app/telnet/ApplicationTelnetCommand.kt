package com.stringconcat.dev.course.app.telnet

import com.khubla.telnet.nvt.NVT
import com.khubla.telnet.shell.command.TelnetCommand
import java.util.HashMap
import java.util.UUID

abstract class ApplicationTelnetCommand : TelnetCommand {

    override fun execute(nvt: NVT, line: String, sessionParameters: HashMap<String, Any>, sessionId: UUID): Boolean {
        nvt.writeln(execute(line, sessionParameters, sessionId).replace("\n", "\r\n"))
        return true
    }

    abstract fun execute(line: String, sessionParameters: Map<String, Any>, sessionId: UUID): String
}