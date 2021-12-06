package com.stringconcat.ddd.e2e.cases

import com.stringconcat.ddd.e2e.MENU
import com.stringconcat.ddd.e2e.ORDERS
import com.stringconcat.ddd.e2e.TEST_TELNET_PORT
import com.stringconcat.ddd.e2e.Url
import com.stringconcat.ddd.e2e.steps.CartSteps
import com.stringconcat.ddd.e2e.steps.MenuSteps
import com.stringconcat.ddd.e2e.steps.OrderSteps
import com.stringconcat.ddd.e2e.steps.UrlSteps
import com.stringconcat.dev.course.app.TestTelnetClient
import io.qameta.allure.Epic
import io.qameta.allure.Story
import org.junit.jupiter.api.Test
import org.koin.core.KoinComponent
import org.koin.core.inject

@Epic("Deliver an order")
class OrderAndCookCase : KoinComponent {

    val Url by inject<UrlSteps>()
    val Menu by inject<MenuSteps>()
    val Cart by inject<CartSteps>()
    val Order by inject<OrderSteps>()

    @Test
    @Story("Cook an order")
    suspend fun `simple test`() {
        val urls = Url.`Get start links`()
        val mealId = Menu.`Add a new meal`(urls[MENU]!!)
        val telnet = TestTelnetClient("localhost", TEST_TELNET_PORT)
        Cart.`Add meal to cart`(telnet, mealId)
        val orderInfo = Cart.`Create an order`(telnet)
        Order.`Pay for the order`(orderInfo.second)
        val orderUrl = Url.`Get order by id link`(urls[ORDERS]!!, orderInfo.first)
        Order.`Confirm order`(orderUrl)
    }
}