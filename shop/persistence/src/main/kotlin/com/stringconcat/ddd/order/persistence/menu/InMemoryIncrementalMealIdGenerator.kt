package com.stringconcat.ddd.order.persistence.menu

import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.domain.menu.MealIdGenerator
import java.util.concurrent.atomic.AtomicLong

class InMemoryIncrementalMealIdGenerator : MealIdGenerator {

    private val counter = AtomicLong(0)

    override fun generate(): MealId {
        return MealId(counter.incrementAndGet())
    }
}