package com.stringconcat.ddd.common.types.common

import com.stringconcat.ddd.common.types.count
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class CountTest {

    @ParameterizedTest
    @ValueSource(ints = [0, 1, Int.MAX_VALUE])
    fun `create count - success`(value: Int) {
        val result = Count.from(value)
        val count = result.shouldBeRight()
        count.value shouldBe value
    }

    @Test
    fun `create count - one`() {
        val result = Count.one()
        result.value shouldBe 1
    }

    @Test
    fun `create count - negative value`() {
        val result = Count.from(-1)
        result shouldBeLeft NegativeValueError
    }

    @Test
    fun `increment - success`() {
        val count = count(1)
        val increment = count.increment()
        increment shouldBeRight count(count.value + 1)
    }

    @Test
    fun `increment - max value reached`() {
        val count = count(Int.MAX_VALUE)
        val result = count.increment()
        result shouldBeLeft MaxValueReachedError
    }

    @ParameterizedTest
    @ValueSource(ints = [1, Int.MAX_VALUE])
    fun `decrement - success`(value: Int) {
        val count = count(value)
        val increment = count.decrement()
        increment shouldBeRight count(count.value - 1)
    }

    @Test
    fun `decrement - min value reached`() {
        val count = count(0)
        val result = count.decrement()
        result shouldBeLeft MinValueReachedError
    }

    @Test
    fun `check is min value - true`() {
        val count = count(0)
        count.isMin() shouldBe true
    }

    @Test
    fun `check is min value - false`() {
        val count = count(1)
        count.isMin() shouldBe false
    }

    @Test
    fun `check is max value - true`() {
        val count = count(Int.MAX_VALUE)
        count.isMax() shouldBe true
    }

    @Test
    fun `check is max value - false`() {
        val count = count(1)
        count.isMax() shouldBe false
    }
}