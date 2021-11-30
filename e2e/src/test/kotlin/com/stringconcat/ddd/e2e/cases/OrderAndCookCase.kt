package com.stringconcat.ddd.e2e.cases

import com.stringconcat.ddd.e2e.steps.MenuSteps
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.junit.jupiter.api.Test
import org.koin.core.KoinComponent
import org.koin.core.inject

@Epic("Example")
@Feature("Flight")
class OrderAndCookCase : KoinComponent {

    val Menu by inject<MenuSteps>()

    @Test
    @Story("Test story")
    suspend fun `simple test`() {
        Menu.`Say hello`()
    }
}