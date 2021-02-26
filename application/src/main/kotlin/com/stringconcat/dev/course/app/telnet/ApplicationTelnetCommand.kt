package com.stringconcat.dev.course.app.telnet

import com.khubla.telnet.nvt.NVT
import com.khubla.telnet.shell.command.TelnetCommand
import java.util.HashMap

abstract class ApplicationTelnetCommand : TelnetCommand {

    override fun execute(nvt: NVT, line: String, sessionParameters: HashMap<String, Any>): Boolean {
        nvt.writeln(execute(line, sessionParameters).replace("\n", "\r\n"))
        return true
    }

    abstract fun execute(line: String, sessionParameters: Map<String, Any>): String
}