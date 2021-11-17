package com.stringconcat.ddd.common.types.base

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.random.Random
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class VersionTest {

    @Test
    fun `new id - check version is zero`() {
        val firstVersion = Version.new()
        val secondVersion = Version.new()
        firstVersion.value shouldBe secondVersion.value
        firstVersion.value shouldBe 0
        firstVersion.isNew().shouldBeTrue()
    }

    @ParameterizedTest
    @ValueSource(longs = [-1, 12, Long.MAX_VALUE, Long.MIN_VALUE])
    fun `non-zero value isn't the new version`(value: Long) {
        val version = Version.from(value)
        version.isNew().shouldBeFalse()
    }

    @Test
    fun `restore from long`() {
        val long = Random.nextLong()
        val version = Version.from(long)
        version.value shouldBe long
    }

    @Test
    fun `increment counter - value is +1`() {
        val long = Random.nextLong()
        val version = Version.from(long)
        val incremented = version.next()
        incremented.value shouldBe long + 1
    }

    @Test
    fun `the same value should be equals`() {
        val long = Random.nextLong()
        val first = Version.from(long)
        val second = Version.from(long)
        first shouldBe second
    }

    @Test
    fun `previous version should be current - 1`() {
        val long = Random.nextLong()
        val version = Version.from(long)

        val previous = version.previous()

        previous.value shouldBe version.value - 1
    }
}