package com.stringconcat.ddd.common.domain.supertype

abstract class AggregateRoot<T>(id: T , version: Version) : DomainEntity<T>(id, version)