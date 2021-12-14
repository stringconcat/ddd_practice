package com.stringconcat.ddd.e2e.steps

import com.stringconcat.ddd.e2e.ID
import com.stringconcat.ddd.e2e.OrderId
import com.stringconcat.ddd.e2e.Settings
import org.koin.core.KoinComponent
import org.koin.core.inject
import ru.fix.corounit.allure.Step
import ru.fix.kbdd.asserts.get
import ru.fix.kbdd.asserts.isEquals
import ru.fix.kbdd.rest.Rest

open class CrmSteps : KoinComponent {

    val Settings by inject<Settings>()

    @Step
    open suspend fun `Check crm after`(orderId: OrderId) {
        Rest.request {
            baseUri(Settings.crmBaseUrl)
            get("/order")
        }
        Rest.statusCode().isEquals(200)
        Rest.bodyJson()[ID].isEquals(orderId.value)
    }
}