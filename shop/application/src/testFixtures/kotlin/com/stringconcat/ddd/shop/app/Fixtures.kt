package com.stringconcat.ddd.shop.app

import com.stringconcat.ddd.shop.app.event.IntegrationMessagePublisher
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.meal
import com.stringconcat.ddd.shop.domain.menu.Meal
import com.stringconcat.ddd.shop.usecase.menu.access.MealPersister
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

const val TEST_TELNET_PORT = 22121
const val UUID_PATTERN = "\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}"

fun telnetClient() = TestTelnetClient("localhost", TEST_TELNET_PORT)

fun prepareCart(client: TestTelnetClient, mealRepository: MealPersister, meal: Meal = meal()): CustomerId {
    mealRepository.save(meal)

    client.writeCommand("add ${meal.id.toLongValue()}")
    client.lastMessageShouldBeOk()

    client.writeCommand("cart")
    val cartResponse = client.readMessage()
    return cartResponse.extractCustomerId()
}

private fun String.extractCustomerId(): CustomerId {
    val uuidRegex = UUID_PATTERN.toRegex()
    val customerString = uuidRegex.find(this)?.value
    customerString.shouldNotBeNull()
    return CustomerId(customerString)
}

fun String.toServerUrl() = "http://localhost$this"

class MockIntegrationPublisher : IntegrationMessagePublisher {

    lateinit var message: Any

    override fun send(message: Any) {
        this.message = message
    }

    fun verifyZeroInteraction() {
        ::message.isInitialized.shouldBeFalse()
    }

    fun verifyInvoked(message: Any) {
        this.message shouldBe message
    }

}