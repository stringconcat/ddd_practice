package com.stringconcat.ddd.shop.usecase.order.rules

import com.stringconcat.ddd.common.events.DomainEventListener
import com.stringconcat.ddd.shop.domain.order.ShopOrderCreatedDomainEvent
import com.stringconcat.ddd.shop.usecase.order.providers.OrderExporter

class ExportOrderAfterCheckoutRule(private val orderExporter: OrderExporter) :
    DomainEventListener<ShopOrderCreatedDomainEvent> {
    override fun eventType() = ShopOrderCreatedDomainEvent::class

    override fun handle(event: ShopOrderCreatedDomainEvent) {
        orderExporter.exportOrder(event.orderId, event.forCustomer, event.totalPrice)
    }
}