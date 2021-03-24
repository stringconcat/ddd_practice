package com.stringconcat.ddd.shop.usecase.order

import com.stringconcat.ddd.shop.usecase.TestCustomerOrderExtractor
import com.stringconcat.ddd.shop.usecase.order
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import org.junit.jupiter.api.Test

internal class GetOrdersUseCaseTest {

    @Test
    fun `storage is empty`() {

        val extractor = TestCustomerOrderExtractor()
        val useCase = GetOrdersUseCase(extractor)
        val result = useCase.execute()
        result.shouldBeEmpty()
    }

    @Test
    fun `storage is not empty`() {
        val order = order()
        val extractor = TestCustomerOrderExtractor().apply {
            this[order.id] = order
        }

        val useCase = GetOrdersUseCase(extractor)
        val result = useCase.execute()
        result shouldContainExactly listOf(
            CustomerOrderInfo(
                id = order.id,
                state = order.state,
                address = order.address,
                total = order.totalPrice()
            )
        )
    }
}