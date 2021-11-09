package com.stringconcat.ddd.shop.usecase.order.invariants

import com.stringconcat.ddd.shop.domain.customerId
import com.stringconcat.ddd.shop.usecase.TestShopOrderExtractor
import com.stringconcat.ddd.shop.usecase.activeOrder
import com.stringconcat.ddd.shop.usecase.nonActiveOrder
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.api.Test

internal class CustomerHasActiveOrderImplTest {

    @Test
    fun `active order exists`() {

        val activeOrder = activeOrder()
        val extractor = TestShopOrderExtractor().apply {
            this[activeOrder.id] = activeOrder
        }
        val rule = CustomerHasActiveOrderImpl(extractor)

        val hasActiveOrder = rule.check(activeOrder.forCustomer)
        hasActiveOrder.shouldBeTrue()
    }

    @Test
    fun `order exists but not active`() {

        val activeOrder = nonActiveOrder()
        val extractor = TestShopOrderExtractor().apply {
            this[activeOrder.id] = activeOrder
        }
        val rule = CustomerHasActiveOrderImpl(extractor)

        val hasActiveOrder = rule.check(activeOrder.forCustomer)
        hasActiveOrder.shouldBeFalse()
    }

    @Test
    fun `order doesn't exists`() {

        val extractor = TestShopOrderExtractor()
        val rule = CustomerHasActiveOrderImpl(extractor)

        val hasActiveOrder = rule.check(customerId())
        hasActiveOrder.shouldBeFalse()
    }
}