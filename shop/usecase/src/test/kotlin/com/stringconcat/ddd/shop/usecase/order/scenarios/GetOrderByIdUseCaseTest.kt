package com.stringconcat.ddd.shop.usecase.order.scenarios

import com.stringconcat.ddd.shop.domain.order
import com.stringconcat.ddd.shop.domain.orderId
import com.stringconcat.ddd.shop.usecase.MockShopOrderExtractor
import com.stringconcat.ddd.shop.usecase.order.GetOrderByIdUseCaseError
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class GetOrderByIdUseCaseTest {

    @Test
    fun `order not found`() {
        val extractor = MockShopOrderExtractor()
        val useCase = GetOrderByIdUseCase(extractor)

        val orderId = orderId()
        val result = useCase.execute(orderId)

        result shouldBeLeft GetOrderByIdUseCaseError.OrderNotFound
        extractor.verifyInvokedGetById(orderId)
    }

    @Test
    fun `order extracted successfully`() {
        val order = order()
        val extractor = MockShopOrderExtractor(order)
        val useCase = GetOrderByIdUseCase(extractor)

        val result = useCase.execute(order.id)
        val details = result.shouldBeRight()

        details.id shouldBe order.id
        details.address shouldBe order.address
        details.state shouldBe order.state
        details.total shouldBe order.totalPrice()
        details.version shouldBe order.version
        details.readyForConfirmOrCancel shouldBe order.readyForConfirmOrCancel()
        details.items.shouldNotBeEmpty()
        details.items.size shouldBe order.orderItems.size

        details.items.forEach { i ->

            val srcItems = order.orderItems.filter {
                it.mealId == i.mealId && i.count == it.count
            }
            srcItems.shouldHaveSize(1)
        }
        extractor.verifyInvokedGetById(order.id)
    }
}