package com.stringconcat.dev.course.app.configuration.shop

import com.stringconcat.ddd.shop.rest.menu.AddMealToMenuEndpoint
import com.stringconcat.ddd.shop.rest.menu.GetMealByIdEndpoint
import com.stringconcat.ddd.shop.rest.menu.GetMenuEndpoint
import com.stringconcat.ddd.shop.rest.menu.RemoveMealFromMenuEndpoint
import com.stringconcat.ddd.shop.rest.order.CancelOrderEndpoint
import com.stringconcat.ddd.shop.rest.order.ConfirmOrderEndpoint
import com.stringconcat.ddd.shop.rest.order.GetOrderByIdEndpoint
import com.stringconcat.ddd.shop.rest.order.GetOrdersEndpoint
import com.stringconcat.ddd.shop.usecase.menu.AddMealToMenu
import com.stringconcat.ddd.shop.usecase.menu.GetMealById
import com.stringconcat.ddd.shop.usecase.menu.GetMenu
import com.stringconcat.ddd.shop.usecase.menu.RemoveMealFromMenu
import com.stringconcat.ddd.shop.usecase.order.CancelOrder
import com.stringconcat.ddd.shop.usecase.order.ConfirmOrder
import com.stringconcat.ddd.shop.usecase.order.GetOrderById
import com.stringconcat.ddd.shop.usecase.order.GetOrders
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ShopRestConfiguration {

    @Bean
    fun getMenuEndpoint(getMenu: GetMenu) = GetMenuEndpoint(getMenu)

    @Bean
    fun addMealToMenuEndpoint(addMealToMenu: AddMealToMenu) = AddMealToMenuEndpoint(addMealToMenu)

    @Bean
    fun removeMealFromMenuEndpoint(removeMealFromMenu: RemoveMealFromMenu) =
        RemoveMealFromMenuEndpoint(removeMealFromMenu)

    @Bean
    fun getMealByIdEndpoint(getMealById: GetMealById) = GetMealByIdEndpoint(getMealById)

    @Bean
    fun getOrderByIdEndpoint(getOrderById: GetOrderById) = GetOrderByIdEndpoint(getOrderById)

    @Bean
    fun getOrdersEndpoint(getOrders: GetOrders) = GetOrdersEndpoint(getOrders)

    @Bean
    fun confirmOrderEndpoint(confirmOrder: ConfirmOrder) = ConfirmOrderEndpoint(confirmOrder)

    @Bean
    fun cancelOrderEndpoint(cancelOrder: CancelOrder) = CancelOrderEndpoint(cancelOrder)
}