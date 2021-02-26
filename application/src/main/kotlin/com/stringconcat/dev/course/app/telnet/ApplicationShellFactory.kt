package com.stringconcat.dev.course.app.telnet

import com.khubla.telnet.nvt.NVT
import com.khubla.telnet.nvt.spy.NVTSpy
import com.khubla.telnet.shell.ShellFactory
import com.khubla.telnet.shell.command.TelnetCommandRegistry

class ApplicationShellFactory(val commandRegistry: TelnetCommandRegistry) : ShellFactory {

    override fun createShell(nvt: NVT?): ApplicationShell {
        return ApplicationShell(nvt, commandRegistry)
    }

    override fun getNVTSpy(): NVTSpy? {
        return null
    }
}