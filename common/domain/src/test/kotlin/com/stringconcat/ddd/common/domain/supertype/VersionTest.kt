package com.stringconcat.ddd.common.domain.supertype

import io.kotlintest.shouldNotBe
import org.junit.jupiter.api.Test

internal class VersionTest{

    @Test
    fun `generate id - check version is unique`() {
        val firstVersion = Version.generate()
        val secondVersion = Version.generate()
        firstVersion.value shouldNotBe secondVersion.value
    }

}