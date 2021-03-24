package com.stringconcat.ddd.order.usecase.rules

import com.stringconcat.ddd.order.usecase.TestCustomerOrderExtractor
import com.stringconcat.ddd.order.usecase.activeOrder
import com.stringconcat.ddd.order.usecase.customerId
import com.stringconcat.ddd.order.usecase.nonActiveOrder
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.api.Test

internal class CustomerHasActiveOrderImplTest {

    @Test
    fun `active order exists`() {

        val activeOrder = activeOrder()
        val extractor = TestCustomerOrderExtractor().apply {
            this[activeOrder.id] = activeOrder
        }
        val rule = CustomerHasActiveOrderImpl(extractor)

        val hasActiveOrder = rule.check(activeOrder.forCustomer)
        hasActiveOrder.shouldBeTrue()
    }

    @Test
    fun `order exists but not active`() {

        val activeOrder = nonActiveOrder()
        val extractor = TestCustomerOrderExtractor().apply {
            this[activeOrder.id] = activeOrder
        }
        val rule = CustomerHasActiveOrderImpl(extractor)

        val hasActiveOrder = rule.check(activeOrder.forCustomer)
        hasActiveOrder.shouldBeFalse()
    }

    @Test
    fun `order doesn't exists`() {

        val extractor = TestCustomerOrderExtractor()
        val rule = CustomerHasActiveOrderImpl(extractor)

        val hasActiveOrder = rule.check(customerId())
        hasActiveOrder.shouldBeFalse()
    }
}