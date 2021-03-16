package com.stringconcat.dev.course.app.listeners

import com.stringconcat.ddd.order.domain.order.CustomerOrderHasBeenDomainEvent
import com.stringconcat.ddd.order.domain.order.OrderItem
import com.stringconcat.dev.course.app.TestCustomerOrderExtractor
import com.stringconcat.dev.course.app.TestFailedCrmProvider
import com.stringconcat.dev.course.app.TestSuccessfulCrmProvider
import com.stringconcat.dev.course.app.count
import com.stringconcat.dev.course.app.customerOrder
import com.stringconcat.dev.course.app.meal
import com.stringconcat.dev.course.app.orderId
import com.stringconcat.dev.course.app.price
import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Test

/**
 * @author levdokimova on 16.03.2021
 */
class SendOrderToCRMAfterPaymentRuleTest {
    @Test
    fun `order successfully sent to crm`() {
        val meal = meal()
        val price = price()
        val count = count()
        val order = customerOrder(orderItems = setOf(OrderItem(meal.id, price, count)))
        val crmProvider = TestSuccessfulCrmProvider()

        val orderExtractor = TestCustomerOrderExtractor().apply {
            this[order.id] = order
        }

        val rule = SendOrderToCRMAfterPaymentRule(
                customerOrderExtractor = orderExtractor,
                crmProvider = crmProvider
        )

        val event = CustomerOrderHasBeenDomainEvent(order.id)
        rule.handle(event)

        crmProvider.verifyNotZeroInteraction()
    }

    @Test
    fun `order not found`() {
        val orderExtractor = TestCustomerOrderExtractor()
        val crmProvider = TestSuccessfulCrmProvider()

        val rule = SendOrderToCRMAfterPaymentRule(
                customerOrderExtractor = orderExtractor,
                crmProvider = crmProvider
        )

        val event = CustomerOrderHasBeenDomainEvent(orderId())

        shouldThrow<IllegalStateException> {
            rule.handle(event)
        }

        crmProvider.verifyZeroInteraction()
    }

    @Test
    fun `order failed sent to crm`() {
        val meal = meal()
        val price = price()
        val count = count()
        val order = customerOrder(orderItems = setOf(OrderItem(meal.id, price, count)))
        val crmProvider = TestFailedCrmProvider()

        val orderExtractor = TestCustomerOrderExtractor().apply {
            this[order.id] = order
        }

        val rule = SendOrderToCRMAfterPaymentRule(
                customerOrderExtractor = orderExtractor,
                crmProvider = crmProvider
        )

        val event = CustomerOrderHasBeenDomainEvent(order.id)
        shouldThrow<IllegalStateException> {
            rule.handle(event)
        }

        crmProvider.verifyNotZeroInteraction()
    }
}