package com.stringconcat.integration.crm

import arrow.core.Either
import arrow.core.right
import com.stringconcat.ddd.order.domain.menu.Price
import com.stringconcat.ddd.order.domain.order.CustomerOrderId
import com.stringconcat.ddd.order.usecase.order.CrmProvider
import com.stringconcat.ddd.order.usecase.order.CrmSendHandlerError
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author levdokimova on 16.03.2021
 */
class SimpleCrmProvider : CrmProvider {
    private val logger: Logger = LoggerFactory.getLogger(SimpleCrmProvider::class.java)

    override fun send(orderId: CustomerOrderId, price: Price): Either<CrmSendHandlerError, Unit> {
        logger.info("The order ${orderId.value} worth ${price.value} has been paid")
        return Unit.right()
    }
}