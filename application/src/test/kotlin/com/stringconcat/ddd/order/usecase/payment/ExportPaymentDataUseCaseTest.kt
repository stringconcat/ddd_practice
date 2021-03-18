package com.stringconcat.ddd.order.usecase.payment

import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.order.CustomerOrder
import com.stringconcat.ddd.order.domain.order.CustomerOrderId
import com.stringconcat.ddd.order.domain.order.OrderState
import com.stringconcat.ddd.order.domain.payment.OrderPayment
import com.stringconcat.ddd.order.usecase.order.CustomerOrderExtractor
import com.stringconcat.dev.course.app.customerOrder
import com.stringconcat.dev.course.app.orderId
import com.stringconcat.dev.course.app.orderItem
import com.stringconcat.dev.course.app.price
import com.stringconcat.dev.course.app.count
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class ExportPaymentDataUseCaseTest {

    @Test
    fun `should return error on non-existing order type`() {
        val useCase = ExportPaymentDataUseCase(
            paymentExporter = TestPaymentsExporter(),
            orderExtractor = TestCustomerOrderExtractor(order = null)
        )
        useCase.execute(orderId()) shouldBeLeft ExportPaymentDataError.OrderNotFound
    }

    @Test
    fun `should return error if order is not paid`() {
        val order = customerOrder(
            state = OrderState.WAITING_FOR_PAYMENT,
            orderItems = setOf(
                orderItem(price(BigDecimal.valueOf(10)), count(2)),
                orderItem(price(BigDecimal.valueOf(15)), count(1))
            )
        )
        val useCase = ExportPaymentDataUseCase(
            paymentExporter = TestPaymentsExporter(),
            orderExtractor = TestCustomerOrderExtractor(order = order)
        )

        useCase.execute(order.id) shouldBeLeft ExportPaymentDataError.InvalidOrderState
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
        val useCase = ExportPaymentDataUseCase(
            paymentExporter = paymentsExporter,
            orderExtractor = TestCustomerOrderExtractor(order = order)
        )

        useCase.execute(order.id) shouldBeRight Unit

        val orderPayment = paymentsExporter.receivedData.shouldNotBeNull()
        orderPayment.orderId shouldBe order.id
        orderPayment.price shouldBe price(BigDecimal.valueOf(35))
    }

    class TestCustomerOrderExtractor(private val order: CustomerOrder?) : CustomerOrderExtractor {

        override fun getById(orderId: CustomerOrderId): CustomerOrder? {
            if (order != null) {
                orderId shouldBe order.id
            }
            return order
        }

        override fun getLastOrder(forCustomer: CustomerId): CustomerOrder? {
            io.kotest.assertions.fail("Should not be called")
        }

        override fun getAll(): List<CustomerOrder> {
            io.kotest.assertions.fail("Should not be called")
        }
    }

    class TestPaymentsExporter : PaymentExporter {

        var receivedData: OrderPayment? = null

        override fun exportPayment(payment: OrderPayment) {
            receivedData = payment
        }
    }
}
