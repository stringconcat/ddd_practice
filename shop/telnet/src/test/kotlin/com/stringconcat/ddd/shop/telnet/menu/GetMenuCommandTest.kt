package com.stringconcat.ddd.shop.telnet.menu

import com.stringconcat.ddd.shop.domain.mealDescription
import com.stringconcat.ddd.shop.domain.mealId
import com.stringconcat.ddd.shop.domain.mealName
import com.stringconcat.ddd.shop.domain.price
import com.stringconcat.ddd.shop.usecase.menu.GetMenu
import com.stringconcat.ddd.shop.usecase.menu.MealInfo
import io.kotest.matchers.shouldBe
import java.math.BigDecimal
import java.util.UUID
import org.junit.jupiter.api.Test

class GetMenuCommandTest {

    @Test
    fun `get cart - cart is empty`() {

        val menu = TestGetMenu(emptyList())

        val command = GetMenuCommand(menu)
        val result = command.execute(
            line = "",
            sessionParameters = emptyMap(),
            sessionId = UUID.randomUUID()
        )
        result shouldBe
                "╔══╤══════╤═════════════╤═══════╗\n" +
                "║  │ Name │ Description │ Price ║\n" +
                "╠══╪══════╪═════════════╪═══════╣\n" +
                "╚══╧══════╧═════════════╧═══════╝"
    }

    @Test
    fun `get cart - cart is not empty`() {
        val menu = TestGetMenu(
            listOf(
                MealInfo(
                    id = mealId(1),
                    name = mealName("meal name"),
                    description = mealDescription("description"),
                    price = price(BigDecimal.TEN)
                )
            )
        )

        val command = GetMenuCommand(menu)
        val result = command.execute(
            line = "",
            sessionParameters = emptyMap(),
            sessionId = UUID.randomUUID()
        )
        result shouldBe
                "╔═══╤═══════════╤═════════════╤═══════╗\n" +
                "║   │ Name      │ Description │ Price ║\n" +
                "╠═══╪═══════════╪═════════════╪═══════╣\n" +
                "║ 1 │ meal name │ description │ 10.00 ║\n" +
                "╚═══╧═══════════╧═════════════╧═══════╝"
    }

    class TestGetMenu(val menu: List<MealInfo>) : GetMenu {
        override fun execute() = menu
    }
}