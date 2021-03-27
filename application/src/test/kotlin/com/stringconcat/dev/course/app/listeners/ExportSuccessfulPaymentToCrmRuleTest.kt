package com.stringconcat.dev.course.app.listeners

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.order.domain.order.CustomerOrderHasBeenPaidDomainEvent
import com.stringconcat.ddd.order.domain.order.CustomerOrderId
import com.stringconcat.ddd.order.usecase.payment.ExportPaymentData
import com.stringconcat.ddd.order.usecase.payment.ExportPaymentDataError
import com.stringconcat.dev.course.app.orderId
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class ExportSuccessfulPaymentToCrmRuleTest {

    @Test
    fun `should have correct type`() {
        val rule = ExportSuccessfulPaymentToCrmRule(TestExportPaymentData())
        rule.eventType() shouldBe CustomerOrderHasBeenPaidDomainEvent::class
    }

    @Test
    fun `should throw error on non-existing order type`() {
        val rule = ExportSuccessfulPaymentToCrmRule(
            exportPaymentData = TestExportPaymentData(ExportPaymentDataError.OrderNotFound)
        )

        shouldThrow<IllegalStateException> {
            rule.handle(CustomerOrderHasBeenPaidDomainEvent(orderId = orderId()))
        }.message shouldBe ExportPaymentDataError.OrderNotFound.message
    }

    @Test
    fun `should throw error on invalid order state`() {
        val rule = ExportSuccessfulPaymentToCrmRule(
            exportPaymentData = TestExportPaymentData(ExportPaymentDataError.InvalidOrderState)
        )

        shouldThrow<IllegalStateException> {
            rule.handle(CustomerOrderHasBeenPaidDomainEvent(orderId = orderId()))
        }.message shouldBe ExportPaymentDataError.InvalidOrderState.message
    }

    @Test
    fun `should successfully export data`() {
        val exportPaymentData = TestExportPaymentData()
        val orderId = orderId()

        val rule = ExportSuccessfulPaymentToCrmRule(exportPaymentData)
        rule.handle(CustomerOrderHasBeenPaidDomainEvent(orderId = orderId))

        exportPaymentData.receivedOrderId shouldBe orderId
    }

    class TestExportPaymentData(
        private val error: ExportPaymentDataError? = null,
    ) : ExportPaymentData {

        var receivedOrderId: CustomerOrderId? = null

        override fun execute(orderId: CustomerOrderId): Either<ExportPaymentDataError, Unit> {
            receivedOrderId = orderId
            if (error != null) {
                return error.left()
            }

            return Unit.right()
        }
    }
}
