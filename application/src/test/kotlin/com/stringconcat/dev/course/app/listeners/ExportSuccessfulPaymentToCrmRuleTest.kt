package com.stringconcat.dev.course.app.listeners

import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.order.CustomerOrder
import com.stringconcat.ddd.order.domain.order.CustomerOrderHasBeenPaidDomainEvent
import com.stringconcat.ddd.order.domain.order.CustomerOrderId
import com.stringconcat.ddd.order.domain.order.OrderState
import com.stringconcat.ddd.order.domain.payment.OrderPayment
import com.stringconcat.ddd.order.usecase.order.CustomerOrderExtractor
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
    }

    @Test
    fun `should report about payment`() {
        val order = customerOrder(
            state = OrderState.PAID,
            orderItems = setOf(
                orderItem(price(BigDecimal.valueOf(10)), count(2)),
                orderItem(price(BigDecimal.valueOf(15)), count(1))
            )
        )
        val paymentsExporter = TestPaymentsExporter()
        val rule = ExportSuccessfulPaymentToCrmRule(
            paymentExporter = paymentsExporter,
            orderExtractor = TestCustomerOrderExtractor(order)
        )

        rule.handle(CustomerOrderHasBeenPaidDomainEvent(orderId = order.id))

        val orderPayment = paymentsExporter.receivedData.shouldNotBeNull()
        orderPayment.orderId shouldBe order.id
        orderPayment.price shouldBe price(BigDecimal.valueOf(35))
    }

    class TestCustomerOrderExtractor(private val order: CustomerOrder?) : CustomerOrderExtractor {

        override fun getById(orderId: CustomerOrderId): CustomerOrder? {
            return order
        }

        override fun getLastOrder(forCustomer: CustomerId): CustomerOrder? {
            fail("Should not be called")
        }

        override fun getAll(): List<CustomerOrder> {
            fail("Should not be called")
        }
    }

    class TestPaymentsExporter : PaymentExporter {

        var receivedData: OrderPayment? = null

        override fun exportPayment(payment: OrderPayment) {
            receivedData = payment
        }
    }
}
