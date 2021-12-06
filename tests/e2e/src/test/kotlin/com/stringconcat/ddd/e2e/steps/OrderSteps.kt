package com.stringconcat.ddd.e2e.steps

import com.stringconcat.ddd.e2e.Url
import io.qameta.allure.Step
import org.koin.core.KoinComponent
import ru.fix.kbdd.asserts.isEquals
import ru.fix.kbdd.rest.Rest

class OrderSteps : KoinComponent {

    @Step
    suspend fun `Pay for the order`(url: Url) {
        Rest.request {
            post(url.value)
        }
        Rest.statusCode().isEquals(200)
    }

    @Step
    suspend fun `Confirm order`(url: Url) {
        Rest.request {
            put(url.value)
        }
        Rest.statusCode().isEquals(201)
    }
}