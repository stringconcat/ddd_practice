package com.stringconcat.ddd.e2e.steps

import com.stringconcat.ddd.e2e.MealId
import io.qameta.allure.Step
import org.koin.core.KoinComponent

class CartSteps : KoinComponent {

    @Step
    @Suppress("UnusedPrivateMember")
    suspend fun `Add meal to cart`(mealId: MealId) {
//        val client = telnetClient()
//        client.writeCommand("add ${mealId.value}")

//        client.checkSuccess()
    }

    @Step
    suspend fun `Create an order`() {
//        val client = telnetClient()
//        client.writeCommand("checkout ${street()} ${building()}")
//
//        client.telnetResponse().asString() shouldContain "Please follow this URL"
    }
}