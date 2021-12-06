package com.stringconcat.ddd.e2e

import com.github.javafaker.Faker
import com.stringconcat.dev.course.app.TestTelnetClient
import ru.fix.corounit.allure.AllureStep
import ru.fix.kbdd.asserts.AlluredKPath
import ru.fix.kbdd.asserts.KPath
import ru.fix.kbdd.asserts.isEquals
import java.math.BigDecimal
import kotlin.random.Random

const val BASE_URL = "http://localhost:8080"
const val TEST_TELNET_PORT = 2121

const val MENU = "menu"
const val ORDERS = "orders"
const val LINKS = "_links"
const val HREF = "href"

val faker = Faker()

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

fun mealName() = "${faker.food().dish()} [${Random.nextInt()}]"
fun mealDescription() = faker.food().ingredient()!!
fun price() = BigDecimal(Random.nextInt(1, 500000))

data class MealId(val value: Long)
data class OrderId(val value: Long)
data class Url(val value: String)