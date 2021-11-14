package com.stringconcat.ddd.shop.usecase.order.scenarios

import com.stringconcat.ddd.shop.domain.order
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.usecase.order.dto.OrderDetails
import com.stringconcat.ddd.shop.usecase.TestShopOrderExtractor
import com.stringconcat.ddd.shop.usecase.order.GetOrdersUseCaseError
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
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
        val result = useCase.execute(orderId, limit)
        val list = result.shouldBeRight()
        list.shouldBeEmpty()
    }

    @Test
    fun `storage is not empty`() {

        val orderId = ShopOrderId(0)
        val limit = 10

        val order = order(id = orderId)
        val extractor = TestShopOrderExtractor().apply {
            this[order.id] = order
        }

        val useCase = GetOrdersUseCase(extractor) { limit }
        val result = useCase.execute(orderId, limit)
        val list = result.shouldBeRight()

        list shouldContainExactly listOf(
           OrderDetails.from(order)
        )
    }

    @Test
    fun `limit exceed`() {
        val orderId = ShopOrderId(0)
        val limit = 10

        val extractor = TestShopOrderExtractor()
        val useCase = GetOrdersUseCase(extractor) { limit }
        val result = useCase.execute(orderId, limit + 1)
        result shouldBeLeft GetOrdersUseCaseError.LimitExceed(maxSize = 10)
    }
}