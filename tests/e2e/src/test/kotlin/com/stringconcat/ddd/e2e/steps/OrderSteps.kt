package com.stringconcat.ddd.e2e.steps

import com.stringconcat.ddd.e2e.OrderId
import com.stringconcat.ddd.e2e.confirmOrderUrl
import com.stringconcat.ddd.e2e.withId
import io.qameta.allure.Step
import org.koin.core.KoinComponent
import ru.fix.kbdd.asserts.isEquals
import ru.fix.kbdd.rest.Rest

class OrderSteps : KoinComponent {

    @Step
    suspend fun `Confirm order`(id: OrderId) {
        Rest.request {
            put(confirmOrderUrl().withId(id.value))
        }
        Rest.statusCode().isEquals(201)
    }
}