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
const val TEST_TELNET_PORT = 22121

const val API_V1 = "/rest/shop/v1"
const val API_V1_MENU = "$API_V1/menu"
const val API_V1_ORDER = "$API_V1/orders"
const val API_V1_ORDER_CONFIRM_BY_ID = "$API_V1_ORDER/{id}/confirm"

const val PARAMETERS = "?startId=0&limit=10"

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

fun menuUrl() = BASE_URL.plus(API_V1_MENU)
fun ordersUrl() = BASE_URL.plus(API_V1_ORDER).plus(PARAMETERS)
fun confirmOrderUrl() = BASE_URL.plus(API_V1_ORDER_CONFIRM_BY_ID)

fun mealName() = "${faker.food().dish()} [${Random.nextInt()}]"
fun mealDescription() = faker.food().ingredient()!!
fun price() = BigDecimal(Random.nextInt(1, 500000))
fun street() = faker.address().streetName()!!
fun building() = faker.address().streetAddressNumber().toInt() + 1

fun String.withParameter(name: String, value: Any) = this.replace("{$name}", value.toString())
fun String.withId(id: Long) = this.withParameter("id", id)

data class MealId(val value: Long)