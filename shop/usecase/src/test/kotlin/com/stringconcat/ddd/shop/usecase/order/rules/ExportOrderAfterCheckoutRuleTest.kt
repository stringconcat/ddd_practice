package com.stringconcat.ddd.shop.usecase.order.rules

import com.stringconcat.ddd.shop.domain.customerId
import com.stringconcat.ddd.shop.domain.order.ShopOrderCreatedDomainEvent
import com.stringconcat.ddd.shop.domain.orderId
import com.stringconcat.ddd.shop.domain.price
import com.stringconcat.ddd.shop.usecase.MockOrderExporter
import org.junit.jupiter.api.Test

class ExportOrderAfterCheckoutRuleTest {

    @Test
    fun `order has been exported`() {

        val orderId = orderId()
        val customerId = customerId()
        val totalPrice = price()

        val exporter = MockOrderExporter()
        val rule = ExportOrderAfterCheckoutRule(exporter)

        val event = ShopOrderCreatedDomainEvent(
            orderId = orderId,
            forCustomer = customerId,
            totalPrice = totalPrice)

        rule.handle(event)

        exporter.verifyInvoked(id = orderId, customerId = customerId, totalPrice = totalPrice)
    }
}