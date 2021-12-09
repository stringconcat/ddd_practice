package com.stringconcat.ddd.shop.app.event

interface IntegrationMessagePublisher {
    fun send(message: Any)
}