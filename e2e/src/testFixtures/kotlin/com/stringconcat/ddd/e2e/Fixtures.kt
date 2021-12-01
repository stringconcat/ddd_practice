package com.stringconcat.ddd.e2e

import com.stringconcat.dev.course.app.TestTelnetClient
import ru.fix.corounit.allure.AllureStep
import ru.fix.kbdd.asserts.AlluredKPath
import ru.fix.kbdd.asserts.KPath
import ru.fix.kbdd.asserts.isEquals

suspend fun TestTelnetClient.telnetResponse() =
    AlluredKPath(
        parentStep = AllureStep.fromCurrentCoroutineContext(),
        node = this.readMessage(),
        mode = KPath.Mode.IMMEDIATE_ASSERT,
        path = "telnet response"
    )

suspend fun TestTelnetClient.checkSuccess() {
    AlluredKPath(
        parentStep = AllureStep.fromCurrentCoroutineContext(),
        node = this.readMessage(),
        mode = KPath.Mode.IMMEDIATE_ASSERT,
        path = "telnet response"
    ).isEquals(TestTelnetClient.OK_RESPONSE)
}