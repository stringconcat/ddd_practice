package com.stringconcat.ddd.e2e.steps

import com.stringconcat.ddd.e2e.BASE_URL
import com.stringconcat.ddd.e2e.CONFIRM
import com.stringconcat.ddd.e2e.COOK
import com.stringconcat.ddd.e2e.EMBEDDED
import com.stringconcat.ddd.e2e.HREF
import com.stringconcat.ddd.e2e.KITCHEN
import com.stringconcat.ddd.e2e.LINKS
import com.stringconcat.ddd.e2e.MENU
import com.stringconcat.ddd.e2e.ORDERS
import com.stringconcat.ddd.e2e.OrderId
import com.stringconcat.ddd.e2e.SELF
import com.stringconcat.ddd.e2e.START_ID_PARAM
import com.stringconcat.ddd.e2e.Url
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.koin.core.KoinComponent
import ru.fix.corounit.allure.Step
import ru.fix.kbdd.asserts.asString
import ru.fix.kbdd.asserts.filter
import ru.fix.kbdd.asserts.get
import ru.fix.kbdd.asserts.isEquals
import ru.fix.kbdd.asserts.isNotNull
import ru.fix.kbdd.asserts.size
import ru.fix.kbdd.rest.Rest
import ru.fix.kbdd.rest.Rest.bodyJson
import ru.fix.kbdd.rest.Rest.statusCode

open class UrlSteps : KoinComponent {

    @Step
    open suspend fun `Get start links`(): Map<String, Url> {
        Rest.request {
            baseUri(BASE_URL)
            get("/")
        }
        statusCode().isEquals(200)

        val menu = bodyJson()[LINKS][MENU][HREF].asString()
        val orders = bodyJson()[LINKS][ORDERS][HREF].asString()
        val kitchen = bodyJson()[LINKS][KITCHEN][HREF].asString()

        menu.isNotEmpty() shouldBe true
        orders.isNotEmpty() shouldBe true
        orders shouldContain START_ID_PARAM
        kitchen.isNotEmpty() shouldBe true

        return mapOf(MENU to Url(menu), ORDERS to Url(orders), KITCHEN to Url(kitchen))
    }

    @Step
    open suspend fun `Get shop order by id link`(startUrl: Url, id: OrderId): Url {
        val regex = "$START_ID_PARAM\\d+".toRegex()
        val modifiedUrl = startUrl.value.replace(regex, START_ID_PARAM.plus(id.value))
        Rest.request {
            get(modifiedUrl)
        }
        statusCode().isEquals(200)

        val orders = bodyJson()[EMBEDDED][ORDERS]
        orders.isNotNull()

        val curOrders = orders.filter { it["id"].isEquals(id.value) }
        curOrders.size().isEquals(1)

        val curOrder = curOrders[0]
        curOrder.isNotNull()

        val getById = curOrder[LINKS][SELF][HREF]
        getById.isNotNull()

        return Url(getById.asString())
    }

    @Step
    open suspend fun `Get confirm order link`(orderUrl: Url): Url {
        Rest.request {
            get(orderUrl.value)
        }
        statusCode().isEquals(200)

        val confirm = bodyJson()[LINKS][CONFIRM][HREF].asString()
        confirm.isNotEmpty() shouldBe true

        return Url(confirm)
    }

    @Step
    open suspend fun `Get kitchen order by id link`(startUrl: Url, id: OrderId): Url {
        Rest.request {
            get(startUrl.value)
        }
        statusCode().isEquals(200)

        val orders = bodyJson()[EMBEDDED][ORDERS]
        orders.isNotNull()

        val curOrders = orders.filter { it["id"].isEquals(id.value) }
        curOrders.size().isEquals(1)

        val curOrder = curOrders[0]
        curOrder.isNotNull()

        val getById = curOrder[LINKS][SELF][HREF]
        getById.isNotNull()

        return Url(getById.asString())
    }

    @Step
    open suspend fun `Get cook order link`(orderUrl: Url): Url {
        Rest.request {
            get(orderUrl.value)
        }
        statusCode().isEquals(200)

        val cook = bodyJson()[LINKS][COOK][HREF].asString()
        cook.isNotEmpty() shouldBe true

        return Url(cook)
    }
}