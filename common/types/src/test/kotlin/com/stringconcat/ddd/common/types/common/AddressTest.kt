package com.stringconcat.ddd.common.types.common

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class AddressTest {

    @Test
    fun `create address - success`() {

        val street = "Street"
        val building = 15

        val result = Address.from(street, building)
        val address = result.shouldBeRight()
        address.building shouldBe building
        address.street shouldBe street
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " "])
    fun `create address - empty string`(street: String) {
        val result = Address.from(street, 1)
        result shouldBeLeft CreateAddressError.EmptyString
    }

    @ParameterizedTest
    @ValueSource(ints = [0, -1])
    fun `create address - non-positive building`(building: Int) {
        val result = Address.from("Street", building)
        result shouldBeLeft CreateAddressError.NonPositiveBuilding
    }
}