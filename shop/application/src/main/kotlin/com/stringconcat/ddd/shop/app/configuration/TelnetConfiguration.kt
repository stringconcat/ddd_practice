package com.stringconcat.ddd.shop.app.configuration

import com.stringconcat.ddd.shop.telnet.cart.AddMealToCartCommand
import com.stringconcat.ddd.shop.telnet.cart.CheckoutCommand
import com.stringconcat.ddd.shop.telnet.cart.GetCartCommand
import com.stringconcat.ddd.shop.telnet.cart.RemoveMealFromCartCommand
import com.stringconcat.ddd.shop.telnet.menu.GetMenuCommand
import com.stringconcat.ddd.shop.telnet.order.GetLastOrderStateCommand
import com.stringconcat.ddd.shop.usecase.cart.GetCart
import com.stringconcat.ddd.shop.usecase.cart.RemoveMealFromCart
import com.stringconcat.ddd.shop.usecase.cart.scenarios.AddMealToCartUseCase
import com.stringconcat.ddd.shop.usecase.menu.GetMenu
import com.stringconcat.ddd.shop.usecase.order.Checkout
import com.stringconcat.ddd.shop.usecase.order.GetLastOrderState
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TelnetConfiguration {
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

    @Bean
    fun getLastOrderStateCommand(useCase: GetLastOrderState) = GetLastOrderStateCommand(useCase)
}