package com.stringconcat.ddd.e2e.steps

import com.stringconcat.ddd.e2e.E2eTestTelnetClient
import com.stringconcat.ddd.e2e.MealId
import com.stringconcat.ddd.e2e.OrderId
import com.stringconcat.ddd.e2e.Url
import io.kotest.matchers.shouldBe
import org.koin.core.KoinComponent
import ru.fix.corounit.allure.Step
import ru.fix.kbdd.asserts.asString
import ru.fix.kbdd.asserts.isContains

open class CartSteps : KoinComponent {

    @Step
    open suspend fun `Add meal to cart`(client: E2eTestTelnetClient, mealId: MealId) {
        client.writeCommandAndLog("add ${mealId.value}")
        client.checkSuccess()
    }

    @Step
    open suspend fun `Create an order`(client: E2eTestTelnetClient): Pair<OrderId, Url> {
        client.writeCommandAndLog("checkout street 123")

        val response = client.telnetResponse()
        response.isContains("has been created")
        response.isContains("Please follow this URL")

        val match = Regex("#\\d+ ").find(response.asString())!!.value
        val id = match.substring(1, match.length - 1)

        id.isEmpty() shouldBe false

        val url = Regex("http:.+$").find(response.asString())!!.value

        url.isEmpty() shouldBe false

        return Pair(OrderId(id.toLong()), Url(url))
    }
}