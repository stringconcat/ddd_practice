package com.stringconcat.dev.course.app.configuration

import com.khubla.telnet.TelnetServer
import com.khubla.telnet.shell.ShellFactory
import com.khubla.telnet.shell.command.TelnetCommand
import com.khubla.telnet.shell.command.TelnetCommandRegistry
import com.stringconcat.ddd.order.usecase.cart.AddMealToCartUseCase
import com.stringconcat.ddd.order.usecase.cart.GetCart
import com.stringconcat.ddd.order.usecase.cart.RemoveMealFromCart
import com.stringconcat.ddd.order.usecase.menu.GetMenu
import com.stringconcat.ddd.order.usecase.order.Checkout
import com.stringconcat.dev.course.app.telnet.ApplicationShellFactory
import com.stringconcat.dev.course.app.telnet.ApplicationTelnetCommandRegistry
import com.stringconcat.dev.course.app.telnet.order.menu.AddMealToCartCommand
import com.stringconcat.dev.course.app.telnet.order.menu.CheckoutCommand
import com.stringconcat.dev.course.app.telnet.order.menu.GetCartCommand
import com.stringconcat.dev.course.app.telnet.order.menu.GetMenuCommand
import com.stringconcat.dev.course.app.telnet.order.menu.RemoveMealFromCartCommand
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
    fun getMenuCommand(useCase: GetMenu) = GetMenuCommand(useCase)

    @Bean
    fun addMealToCartCommand(useCase: AddMealToCartUseCase) = AddMealToCartCommand(useCase)

    @Bean
    fun removeMealFromCartCommand(useCase: RemoveMealFromCart) = RemoveMealFromCartCommand(useCase)

    @Bean
    fun getCartCommand(useCase: GetCart) = GetCartCommand(useCase)

    @Bean
    fun checkoutCommand(useCase: Checkout) = CheckoutCommand(useCase)
}