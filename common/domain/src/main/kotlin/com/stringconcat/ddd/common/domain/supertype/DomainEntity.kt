package com.stringconcat.ddd.common.domain.supertype

import kotlin.random.Random

abstract class DomainEntity<T>(val id: T, val version: Version)

class Version internal constructor(val value: Long) : ValueObject {
    companion object {
        fun generate() = Version(Random.nextLong())
    }
}