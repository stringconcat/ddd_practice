package com.stringconcat.ddd.e2e.steps

import com.stringconcat.ddd.e2e.COOK
import com.stringconcat.ddd.e2e.EMBEDDED
import com.stringconcat.ddd.e2e.HREF
import com.stringconcat.ddd.e2e.LINKS
import com.stringconcat.ddd.e2e.ORDERS
import com.stringconcat.ddd.e2e.OrderId
import com.stringconcat.ddd.e2e.SELF
import com.stringconcat.ddd.e2e.Url
import io.kotest.matchers.shouldBe
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