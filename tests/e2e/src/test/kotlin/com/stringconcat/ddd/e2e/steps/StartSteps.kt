package com.stringconcat.ddd.e2e.steps

import com.stringconcat.ddd.e2e.BASE_URL
import com.stringconcat.ddd.e2e.HREF
import com.stringconcat.ddd.e2e.LINKS
import com.stringconcat.ddd.e2e.MENU
import com.stringconcat.ddd.e2e.ORDERS
import io.kotest.matchers.shouldBe
import io.qameta.allure.Step
import org.koin.core.KoinComponent
import ru.fix.kbdd.asserts.asString
import ru.fix.kbdd.asserts.get
import ru.fix.kbdd.asserts.isEquals
import ru.fix.kbdd.rest.Rest
import ru.fix.kbdd.rest.Rest.bodyJson
import ru.fix.kbdd.rest.Rest.statusCode

class StartSteps : KoinComponent {

    @Step
    suspend fun `Get start links`(): Map<String, String> {
        Rest.request {
            baseUri(BASE_URL)
            get("/")
        }
        statusCode().isEquals(200)

        val menu = bodyJson()[LINKS][MENU][HREF].asString()
        val orders = bodyJson()[LINKS][ORDERS][HREF].asString()

        menu.isNotEmpty() shouldBe true
        orders.isNotEmpty() shouldBe true

        return mapOf(MENU to menu, ORDERS to orders)
    }
}