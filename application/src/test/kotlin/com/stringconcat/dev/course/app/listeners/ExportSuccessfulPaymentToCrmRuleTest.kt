package com.stringconcat.dev.course.app.listeners

import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.order.CustomerOrder
import com.stringconcat.ddd.order.domain.order.CustomerOrderHasBeenPaidDomainEvent
import com.stringconcat.ddd.order.domain.order.CustomerOrderId
import com.stringconcat.ddd.order.domain.order.OrderState
import com.stringconcat.ddd.order.domain.payment.OrderPayment
import com.stringconcat.ddd.order.usecase.order.CustomerOrderExtractor
import com.stringconcat.ddd.order.usecase.payment.PaymentExporter
import com.stringconcat.dev.course.app.count
import com.stringconcat.dev.course.app.customerOrder
import com.stringconcat.dev.course.app.orderItem
import com.stringconcat.dev.course.app.orderId
import com.stringconcat.dev.course.app.price
import io.kotest.assertions.fail
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class ExportSuccessfulPaymentToCrmRuleTest {
/*
    @Test
    fun `should have correct type`() {
        val rule = ExportSuccessfulPaymentToCrmRule(
            paymentExporter = TestPaymentsExporter(),
            orderExtractor = TestCustomerOrderExtractor(order = customerOrder())
        )

        rule.eventType() shouldBe CustomerOrderHasBeenPaidDomainEvent::class
    }

    @Test
    fun `should throw error on non-existing order type`() {
        val rule = ExportSuccessfulPaymentToCrmRule(
            paymentExporter = TestPaymentsExporter(),
            orderExtractor = TestCustomerOrderExtractor(order = null)
        )

        shouldThrow<IllegalStateException> {
            rule.handle(CustomerOrderHasBeenPaidDomainEvent(orderId = orderId()))
        }
    }*/
}
