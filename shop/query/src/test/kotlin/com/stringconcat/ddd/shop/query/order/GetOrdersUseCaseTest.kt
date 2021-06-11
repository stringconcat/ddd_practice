package com.stringconcat.ddd.shop.query.order

import com.stringconcat.ddd.shop.query.TestShopOrderHandlerStub
import com.stringconcat.ddd.shop.query.shopOrderInfo
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import org.junit.jupiter.api.Test

internal class GetOrdersUseCaseTest {

    @Test
    fun `storage is empty`() {

        val handler = TestShopOrderHandlerStub(result = emptyList())
        val useCase = GetOrdersUseCase(handler)
        val result = useCase.execute()
        result.shouldBeEmpty()
    }

    @Test
    fun `storage is not empty`() {
        val shopOrderInfo = shopOrderInfo()
        val extractor = TestShopOrderHandlerStub(result = listOf(shopOrderInfo))

        val useCase = GetOrdersUseCase(extractor)
        val result = useCase.execute()
        result.shouldHaveSize(1)
        result shouldContainExactly listOf(shopOrderInfo)
    }
}