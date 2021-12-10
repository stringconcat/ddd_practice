package com.stringconcat.ddd.e2e.cases

import com.stringconcat.ddd.e2e.E2eTestTelnetClient
import com.stringconcat.ddd.e2e.KITCHEN
import com.stringconcat.ddd.e2e.MENU
import com.stringconcat.ddd.e2e.MealId
import com.stringconcat.ddd.e2e.ORDERS
import com.stringconcat.ddd.e2e.OrderId
import com.stringconcat.ddd.e2e.TEST_TELNET_PORT
import com.stringconcat.ddd.e2e.Url
import com.stringconcat.ddd.e2e.steps.CartSteps
import com.stringconcat.ddd.e2e.steps.CrmSteps
import com.stringconcat.ddd.e2e.steps.MenuSteps
import com.stringconcat.ddd.e2e.steps.OrderSteps
import com.stringconcat.ddd.e2e.steps.UrlSteps
import io.qameta.allure.Epic
import io.qameta.allure.Story
import org.junit.jupiter.api.Test
import org.koin.core.KoinComponent
import org.koin.core.inject
import ru.fix.corounit.allure.invoke
import ru.fix.corounit.allure.repeatUntilSuccess

@Epic("Deliver an order")
class OrderAndCookCase : KoinComponent {

    val Url by inject<UrlSteps>()
    val Menu by inject<MenuSteps>()
    val Cart by inject<CartSteps>()
    val Order by inject<OrderSteps>()

    val Crm by inject<CrmSteps>()

    @Test
    @Story("Cook an order")
    suspend fun `cook an order test`() {

        val urls = Url.`Get start links`()
        val telnet = E2eTestTelnetClient("localhost", TEST_TELNET_PORT)

        var mealId = MealId(0)
        "Prepare menu" {
            mealId = Menu.`Add a new meal`(urls[MENU]!!)
        }

        var orderInfo = Pair(OrderId(0), Url(""))
        "Build and confirm order" {
            Cart.`Add meal to cart`(telnet, mealId)
            orderInfo = Cart.`Create an order`(telnet)
            Order.`Pay for the order`(orderInfo.second)
            val orderShopByIdUrl = Url.`Get shop order by id link`(urls[ORDERS]!!, orderInfo.first)
            val confirmUrl = Url.`Get confirm order link`(orderShopByIdUrl)
            Order.`Confirm order`(confirmUrl)
        }

        "Cook an order" {
            repeatUntilSuccess {
                val orderKitchenByIdUrl = Url.`Get kitchen order by id link`(urls[KITCHEN]!!, orderInfo.first)
                val cookUrl = Url.`Get cook order link`(orderKitchenByIdUrl)
                Order.`Cook order`(cookUrl)
            }
        }

        "Check crm after" {
            Crm.`Check crm after`(orderInfo.first)
        }
    }
}