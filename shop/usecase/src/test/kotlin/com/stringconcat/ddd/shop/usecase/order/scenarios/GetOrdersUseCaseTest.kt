package com.stringconcat.ddd.shop.usecase.order.scenarios

import com.stringconcat.ddd.shop.domain.order
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.usecase.TestShopOrderExtractor
import com.stringconcat.ddd.shop.usecase.order.ShopOrderInfo
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import org.junit.jupiter.api.Test

internal class GetOrdersUseCaseTest {

    @Test
    fun `storage is empty`() {
        val orderId = ShopOrderId(0)
        val limit = 10

        val extractor = TestShopOrderExtractor()
        val useCase = GetOrdersUseCase(extractor) { limit }
        val result = useCase.execute(orderId)
        result.shouldBeEmpty()
    }

    @Test
    fun `storage is not empty`() {

        val orderId = ShopOrderId(0)
        val limit = 10

        val order = order()
        val extractor = TestShopOrderExtractor().apply {
            this[order.id] = order
        }

        val useCase = GetOrdersUseCase(extractor) { limit }
        val result = useCase.execute(orderId)
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