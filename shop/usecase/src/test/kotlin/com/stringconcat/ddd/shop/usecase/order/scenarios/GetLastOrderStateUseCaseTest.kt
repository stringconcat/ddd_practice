package com.stringconcat.ddd.shop.usecase.order.scenarios

import com.stringconcat.ddd.shop.domain.customerId
import com.stringconcat.ddd.shop.domain.order
import com.stringconcat.ddd.shop.usecase.TestShopOrderExtractor
import com.stringconcat.ddd.shop.usecase.order.GetLastOrderStateUseCaseError
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import org.junit.jupiter.api.Test

class GetLastOrderStateUseCaseTest {

    @Test
    fun `status successfully received`() {
        val order = order()
        val orderExtractor = TestShopOrderExtractor().apply {
            this[order.id] = order
        }
        val useCase = GetLastOrderStateUseCase(orderExtractor)
        val result = useCase.execute(order.forCustomer)
        result shouldBeRight order.state
    }

    @Test
    fun `order not found`() {
        val orderExtractor = TestShopOrderExtractor()
        val useCase = GetLastOrderStateUseCase(orderExtractor)
        val result = useCase.execute(customerId())
        result shouldBeLeft GetLastOrderStateUseCaseError.OrderNotFound
    }
}