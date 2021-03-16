package com.stringconcat.ddd.order.usecase.order

import arrow.core.Either
import com.stringconcat.ddd.order.domain.menu.Price
import com.stringconcat.ddd.order.domain.order.CustomerOrderId

interface CrmProvider {
    fun send(orderId: CustomerOrderId, price: Price): Either<CrmSendHandlerError, Unit>
}

sealed class CrmSendHandlerError(val message: String) {
    object OrderNotSentToCrm : CrmSendHandlerError("Order was not sent")
}