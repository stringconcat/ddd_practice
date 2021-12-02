package com.stringconcat.ddd.e2e.cases

import com.stringconcat.ddd.e2e.steps.CartSteps
import com.stringconcat.ddd.e2e.steps.MenuSteps
import com.stringconcat.ddd.e2e.steps.OrderSteps
import com.stringconcat.ddd.e2e.steps.StartSteps
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.junit.jupiter.api.Test
import org.koin.core.KoinComponent
import org.koin.core.inject

@Epic("Example")
@Feature("Flight")
class OrderAndCookCase : KoinComponent {

    val Start by inject<StartSteps>()
    val Menu by inject<MenuSteps>()
    val Cart by inject<CartSteps>()
    val Order by inject<OrderSteps>()

    @Test
    @Story("Test story")
    suspend fun `simple test`() {
        //ToDo м.б. стоит возвращать и передавать link как параметр
        Start.`Get start links`()
        val mealId = Menu.`Add a new meal`()
        Cart.`Add meal to cart`(mealId)
        Cart.`Create an order`()
        val orderId = 0L //ToDo откуда берется orderId?
        Order.`Confirm order`(orderId)
    }
}