package com.stringconcat.ddd.shop.domain.cart

import com.stringconcat.ddd.common.types.base.ValueObject

data class CustomerId(private val value: String) : ValueObject {
    fun toStringValue() = value
}