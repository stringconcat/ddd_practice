package com.stringconcat.ddd.e2e.steps

import com.stringconcat.ddd.e2e.CRM_BASE_URL
import com.stringconcat.ddd.e2e.OrderId
import org.koin.core.KoinComponent
import ru.fix.corounit.allure.Step
import ru.fix.kbdd.asserts.isEquals
import ru.fix.kbdd.rest.Rest

open class CrmSteps : KoinComponent {

    @Step
    open suspend fun `Check crm after`(orderId: OrderId) {
        Rest.request {
            baseUri(CRM_BASE_URL)
            get("/order")
        }
        Rest.statusCode().isEquals(200)
        Rest.bodyString().isEquals(orderId.value)
    }
}