package com.stringconcat.ddd.common.types.base

import io.kotest.matchers.shouldBe
import kotlin.random.Random
import org.junit.jupiter.api.Test

internal class VersionTest {

    @Test
    fun `new id - check version is zero`() {
        val firstVersion = Version.new()
        val secondVersion = Version.new()
        firstVersion.toLongValue() shouldBe secondVersion.toLongValue()
        firstVersion.toLongValue() shouldBe 0
    }

    @Test
    fun `restore from long`() {
        val long = Random.nextLong()
        val version = Version.from(long)
        version.toLongValue() shouldBe long
    }

    @Test
    fun `increment counter - value is +1`() {
        val long = Random.nextLong()
        val version = Version.from(long)
        val incremented = version.next()
        incremented.toLongValue() shouldBe long + 1
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

        previous.toLongValue() shouldBe version.toLongValue() - 1
    }
}