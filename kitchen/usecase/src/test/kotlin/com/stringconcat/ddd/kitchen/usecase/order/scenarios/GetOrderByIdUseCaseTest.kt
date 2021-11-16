package com.stringconcat.ddd.kitchen.usecase.order.scenarios

import com.stringconcat.ddd.kitchen.domain.order.order
import com.stringconcat.ddd.kitchen.domain.order.orderId
import com.stringconcat.ddd.kitchen.usecase.order.GetOrderByIdUseCaseError
import com.stringconcat.ddd.kitchen.usecase.order.TestKitchenOrderExtractor
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class GetOrderByIdUseCaseTest {

    @Test
    fun `order not found`() {
        val extractor = TestKitchenOrderExtractor()
        val useCase = GetOrderByIdUseCase(extractor)
        val result = useCase.execute(orderId())
        result shouldBeLeft GetOrderByIdUseCaseError.OrderNotFound
    }

    @Test
    fun `order extracted successfully`() {
        val order = order()
        val extractor = TestKitchenOrderExtractor().apply {
            this[order.id] = order
        }
        val useCase = GetOrderByIdUseCase(extractor)

        val result = useCase.execute(order.id)
        val details = result.shouldBeRight()

        details.id shouldBe order.id
        details.cooked shouldBe order.cooked
        details.meals.size shouldBe order.meals.size

        details.meals.forEach { i ->

            val srcItems = order.meals.filter {
                it.meal == i.meal && i.count == it.count
            }
            srcItems.shouldHaveSize(1)
        }
    }
}
