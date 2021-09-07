package com.stringconcat.ddd.kitchen.usecase.order.scenarios

import com.stringconcat.ddd.kitchen.usecase.order.KitchenOrderInfo
import com.stringconcat.ddd.kitchen.usecase.order.TestKitchenOrderExtractor
import com.stringconcat.ddd.kitchen.usecase.order.order
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldNotBeEmpty
import org.junit.jupiter.api.Test

class GetOrdersUseCaseTest {

    @Test
    fun `storage is empty`() {

        val extractor = TestKitchenOrderExtractor()
        val useCase = GetOrdersUseCase(extractor)
        val result = useCase.execute()
        result.shouldBeEmpty()
    }

    @Test
    fun `storage is not empty`() {
        val order = order()
        val extractor = TestKitchenOrderExtractor().apply {
            this[order.id] = order
        }

        val useCase = GetOrdersUseCase(extractor)
        val result = useCase.execute()
        result shouldContainExactly listOf(
            KitchenOrderInfo(
                id = order.id,
                meals = order.meals,
                cooked = order.cooked
            )
        )
        order.meals.shouldNotBeEmpty()
    }
}