package com.stringconcat.ddd.e2e

import com.github.javafaker.Faker
import com.stringconcat.ddd.shop.app.TestTelnetClient
import java.math.BigDecimal
import kotlin.random.Random
import ru.fix.corounit.allure.AllureStep
import ru.fix.kbdd.asserts.AlluredKPath
import ru.fix.kbdd.asserts.KPath
import ru.fix.kbdd.asserts.isEquals

const val SHOP_BASE_URL = "http://localhost:8081"
const val KITCHEN_BASE_URL = "http://localhost:8080"
const val TEST_TELNET_PORT = 2121

const val MENU = "menu"
const val ORDERS = "orders"
const val KITCHEN = "kitchen"
const val LINKS = "_links"
const val HREF = "href"
const val SELF = "self"
const val CONFIRM = "confirm"
const val COOK = "cook"
const val EMBEDDED = "_embedded"
const val START_ID_PARAM = "startId="

val faker = Faker()

class E2eTestTelnetClient(
    private val host: String,
    private val port: Int,
) : TestTelnetClient(host, port) {

    suspend fun telnetResponse(): AlluredKPath {
        val step = AllureStep.fromCurrentCoroutineContext()
        val message = this.readMessage()
        step.attachment("Telnet response", message)

        return AlluredKPath(
            parentStep = step,
            node = message,
            mode = KPath.Mode.IMMEDIATE_ASSERT,
            path = "telnet response"
        )
    }

    suspend fun writeCommandAndLog(command: String) {
        val step = AllureStep.fromCurrentCoroutineContext()
        step.attachment("Telnet request", command)
        writeCommand(command)
    }

    suspend fun checkSuccess() {
        val step = AllureStep.fromCurrentCoroutineContext()
        val message = this.readMessage()
        step.attachment("Telnet response", message)

        AlluredKPath(
            parentStep = step,
            node = message,
            mode = KPath.Mode.IMMEDIATE_ASSERT,
            path = "Telnet response"
        ).isEquals(OK_RESPONSE)
    }

    override fun toString(): String {
        return "E2eTestTelnetClient(host='$host', port=$port)"
    }
}

fun mealName() = "${faker.food().dish()} [${Random.nextInt()}]"
fun mealDescription() = faker.food().ingredient()!!
fun price() = BigDecimal(Random.nextInt(1, 500000))

data class MealId(val value: Long)
data class OrderId(val value: Long)
data class Url(val value: String)