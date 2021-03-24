package com.stringconcat.dev.course.app.telnet.order.menu

import com.stringconcat.ddd.shop.usecase.menu.GetMenu
import com.stringconcat.ddd.shop.usecase.menu.MealInfo
import com.stringconcat.dev.course.app.mealDescription
import com.stringconcat.dev.course.app.mealId
import com.stringconcat.dev.course.app.mealName
import com.stringconcat.dev.course.app.price
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.UUID

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