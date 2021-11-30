package com.stringconcat.ddd.e2e.steps

import io.qameta.allure.Step
import org.koin.core.KoinComponent
import ru.fix.kbdd.asserts.isEquals
import ru.fix.kbdd.rest.Rest
import ru.fix.kbdd.rest.Rest.statusCode

class MenuSteps : KoinComponent {

    @Step
    suspend fun `Say hello`() {
        Rest.request {
            baseUri("http://localhost:8080")
            body {
                "amount" {
                    "12" % "22"
                }
            }
            post("/ololo")
        }
        statusCode().isEquals(200)
    }
}