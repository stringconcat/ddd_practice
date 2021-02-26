package com.stringconcat.dev.course.app.telnet.order.menu

import com.stringconcat.ddd.order.usecase.menu.GetMenu
import com.stringconcat.ddd.order.usecase.menu.MealInfo
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.UUID

class GetMenuCommandTest {

    @Test
    fun `get cart - cart is empty`() {

        val command = GetMenuCommand(EmptyUseCase)
        val result = command.execute("", emptyMap(), UUID.randomUUID())
        result shouldBe
                "╔══╤══════╤═════════════╤═══════╗\n" +
                "║  │ Name │ Description │ Price ║\n" +
                "╠══╪══════╪═════════════╪═══════╣\n" +
                "╚══╧══════╧═════════════╧═══════╝"
    }

    @Test
    fun `get cart - cart is not empty`() {
        val command = GetMenuCommand(NotEmptyUseCase)
        val result = command.execute("", emptyMap(), UUID.randomUUID())
        result shouldBe
                "╔═══╤═══════════╤═════════════╤═══════╗\n" +
                "║   │ Name      │ Description │ Price ║\n" +
                "╠═══╪═══════════╪═════════════╪═══════╣\n" +
                "║ 1 │ meal name │ description │    10 ║\n" +
                "╚═══╧═══════════╧═════════════╧═══════╝"
    }

    private object EmptyUseCase : GetMenu {
        override fun execute() = emptyList<MealInfo>()
    }

    private object NotEmptyUseCase : GetMenu {
        override fun execute() =
            listOf(MealInfo(id = 1, name = "meal name", description = "description", price = BigDecimal.TEN))
    }
}