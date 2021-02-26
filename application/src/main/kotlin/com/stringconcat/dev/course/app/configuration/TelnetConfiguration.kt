package com.stringconcat.dev.course.app.configuration

import com.khubla.telnet.TelnetServer
import com.khubla.telnet.shell.ShellFactory
import com.khubla.telnet.shell.command.TelnetCommand
import com.khubla.telnet.shell.command.TelnetCommandRegistry
import com.stringconcat.ddd.order.usecase.menu.GetMenuUseCase
import com.stringconcat.dev.course.app.telnet.ApplicationShellFactory
import com.stringconcat.dev.course.app.telnet.ApplicationTelnetCommandRegistry
import com.stringconcat.dev.course.app.telnet.order.menu.GetMenuCommand
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TelnetConfiguration(
    @Value("\${telnet.port}")
    private val telnetPort: Int,
    @Value("\${telnet.threads}")
    private val telnetThreads: Int
) {

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    fun telnetServer(shellFactory: ShellFactory) = TelnetServer(telnetPort, telnetThreads, shellFactory)

    @Bean
    fun shellFactory(commandRegistry: TelnetCommandRegistry) = ApplicationShellFactory(commandRegistry)

    @Bean
    fun commandRegistry(commands: List<TelnetCommand>) = ApplicationTelnetCommandRegistry(commands)

    @Bean
    fun getMenuCommand(useCase: GetMenuUseCase) = GetMenuCommand(useCase)
}