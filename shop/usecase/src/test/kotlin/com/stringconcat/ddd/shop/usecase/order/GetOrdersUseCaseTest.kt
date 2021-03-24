package com.stringconcat.ddd.shop.usecase.order

import com.stringconcat.ddd.shop.usecase.TestShopOrderExtractor
import com.stringconcat.ddd.shop.usecase.order
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import org.junit.jupiter.api.Test

internal class GetOrdersUseCaseTest {

    @Test
    fun `storage is empty`() {

        val extractor = TestShopOrderExtractor()
        val useCase = GetOrdersUseCase(extractor)
        val result = useCase.execute()
        result.shouldBeEmpty()
    }

    @Test
    fun `storage is not empty`() {
        val order = order()
        val extractor = TestShopOrderExtractor().apply {
            this[order.id] = order
        }

        val useCase = GetOrdersUseCase(extractor)
        val result = useCase.execute()
        result shouldContainExactly listOf(
            ShopOrderInfo(
                id = order.id,
                state = order.state,
                address = order.address,
                total = order.totalPrice()
            )
        )
    }
}