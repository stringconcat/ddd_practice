package com.stringconcat.ddd.common.rest

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class CursorPagedModelTest {
    @Test
    fun `create cursor paged model`() {
        val list = listOf(1, 2, 3, 4, 5)
        val nextId = 6

        val model = CursorPagedModel(list, nextId)

        model.count shouldBe list.size
        model.list shouldBe list
        model.next shouldBe nextId
    }
}