package com.stringconcat.ddd.kitchen.app.events

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.amqp.rabbit.annotation.RabbitHandler

abstract class IntegrationEventListener<T>(private val clazz: Class<T>) {

    private val objectMapper = jacksonObjectMapper()
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

    @RabbitHandler
    fun parseMessage(message: String) {
        val obj = objectMapper.readValue(message, clazz)
        onMessage(obj)
    }

    abstract fun onMessage(message: T)
}