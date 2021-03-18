package com.stringconcat.integration.crm

import com.stringconcat.ddd.order.domain.payment.OrderPayment
import com.stringconcat.dev.course.app.listeners.PaymentExporter
import org.slf4j.LoggerFactory

class SimpleCRMConnector : PaymentExporter {

    private val logger = LoggerFactory.getLogger(SimpleCRMConnector::class.java)

    override fun exportPayment(payment: OrderPayment) {
        logger.info("Payment for the order ${payment.orderId}: ${payment.price}")
    }
}
