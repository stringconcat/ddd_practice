package com.stringconcat.ddd.common.types.base

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class VersionTest {

    @Test
    fun `new id - check version is zero`() {
        val firstVersion = Version.new()
        val secondVersion = Version.new()
        firstVersion.value shouldBe secondVersion.value
        firstVersion.value shouldBe 0
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
        val incremented = version.increment()
        incremented.value shouldBe long + 1
    }
}