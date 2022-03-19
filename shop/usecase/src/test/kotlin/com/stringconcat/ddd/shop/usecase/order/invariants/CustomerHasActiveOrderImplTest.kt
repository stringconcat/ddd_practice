package com.stringconcat.ddd.shop.usecase.order.invariants

import com.stringconcat.ddd.shop.domain.customerId
import com.stringconcat.ddd.shop.usecase.MockShopOrderExtractor
import com.stringconcat.ddd.shop.usecase.activeOrder
import com.stringconcat.ddd.shop.usecase.nonActiveOrder
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.api.Test

internal class CustomerHasActiveOrderImplTest {

    @Test
    fun `active order exists`() {

        val activeOrder = activeOrder()
        val extractor = MockShopOrderExtractor(activeOrder)
        val rule = CustomerHasActiveOrderImpl(extractor)

        val hasActiveOrder = rule(activeOrder.forCustomer)

        hasActiveOrder.shouldBeTrue()
        extractor.verifyInvokedGetLastOrder(activeOrder.forCustomer)
    }

    @Test
    fun `order exists but not active`() {

        val activeOrder = nonActiveOrder()
        val extractor = MockShopOrderExtractor(activeOrder)
        val rule = CustomerHasActiveOrderImpl(extractor)

        val hasActiveOrder = rule(activeOrder.forCustomer)

        hasActiveOrder.shouldBeFalse()
        extractor.verifyInvokedGetLastOrder(activeOrder.forCustomer)
    }

    @Test
    fun `order doesn't exists`() {

        val extractor = MockShopOrderExtractor()
        val customerhasActiveOrder = CustomerHasActiveOrderImpl(extractor)

        val customerId = customerId()
        val hasActiveOrder = customerhasActiveOrder(customerId)

        hasActiveOrder.shouldBeFalse()
        extractor.verifyInvokedGetLastOrder(customerId)
    }
}