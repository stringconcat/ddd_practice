package com.stringconcat.ddd.shop.persistence.menu

import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.MealIdGenerator
import java.util.concurrent.atomic.AtomicLong

class InMemoryIncrementalMealIdGenerator : MealIdGenerator {

    private val counter = AtomicLong(0)

    override fun generate(): MealId {
        return MealId(counter.incrementAndGet())
    }
}