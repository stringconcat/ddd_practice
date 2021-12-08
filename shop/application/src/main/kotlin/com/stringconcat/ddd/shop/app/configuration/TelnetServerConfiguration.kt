package com.stringconcat.ddd.shop.app.configuration

import com.khubla.telnet.TelnetServer
import com.khubla.telnet.shell.ShellFactory
import com.khubla.telnet.shell.command.TelnetCommand
import com.khubla.telnet.shell.command.TelnetCommandRegistry
import com.stringconcat.ddd.shop.app.telnet.ApplicationShellFactory
import com.stringconcat.ddd.shop.app.telnet.ApplicationTelnetCommandRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TelnetServerConfiguration(
    @Value("\${telnet.port}")
    private val telnetPort: Int,
    @Value("\${telnet.threads}")
    private val telnetThreads: Int
) {

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    fun telnetServer(shellFactory: ShellFactory) =
        TelnetServer(telnetPort, telnetThreads, shellFactory)

    @Bean
    fun shellFactory(commandRegistry: TelnetCommandRegistry) = ApplicationShellFactory(commandRegistry)

    @Bean
    fun commandRegistry(commands: List<TelnetCommand>) = ApplicationTelnetCommandRegistry(commands)
}