package com.stringconcat.dev.course.app

import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.meal
import com.stringconcat.ddd.shop.usecase.menu.access.MealPersister
import io.kotest.matchers.nulls.shouldNotBeNull

const val TEST_TELNET_PORT = 22121

fun telnetClient() = TestTelnetClient("localhost", TEST_TELNET_PORT)

fun prepareCart(client: TestTelnetClient, mealRepository: MealPersister): CustomerId {
    val meal = meal()
    mealRepository.save(meal)

    client.writeCommand("add ${meal.id.value}")
    client.lastMessageShouldBeOk()

    client.writeCommand("cart")
    val cartResponse = client.readMessage()
    return cartResponse.extractCustomerId()
}

private fun String.extractCustomerId(): CustomerId {
    val uuidRegex = "\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}".toRegex()
    val customerString = uuidRegex.find(this)?.value
    customerString.shouldNotBeNull()
    return CustomerId(customerString)
}