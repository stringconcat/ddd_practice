package com.stringconcat.ddd.shop.persistence.postgresql

class RaceConditionException(override val message: String?) : RuntimeException(message)