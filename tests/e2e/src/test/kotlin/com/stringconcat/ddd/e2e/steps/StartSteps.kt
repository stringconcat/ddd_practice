package com.stringconcat.ddd.e2e.steps

import com.stringconcat.ddd.e2e.BASE_URL
import com.stringconcat.ddd.e2e.menuUrl
import com.stringconcat.ddd.e2e.ordersUrl
import io.qameta.allure.Step
import org.koin.core.KoinComponent
import ru.fix.kbdd.asserts.get
import ru.fix.kbdd.asserts.isEquals
import ru.fix.kbdd.rest.Rest
import ru.fix.kbdd.rest.Rest.bodyJson
import ru.fix.kbdd.rest.Rest.statusCode

class StartSteps : KoinComponent {

    @Step
    suspend fun `Get start links`() {
        Rest.request {
            baseUri(BASE_URL)
            get("/")
        }
        statusCode().isEquals(200)
        bodyJson()["_links"]["menu"]["href"].isEquals(menuUrl())
        bodyJson()["_links"]["orders"]["href"].isEquals(ordersUrl())
    }
}