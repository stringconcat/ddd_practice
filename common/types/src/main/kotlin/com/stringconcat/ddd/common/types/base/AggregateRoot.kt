package com.stringconcat.ddd.common.types.base

abstract class AggregateRoot<T>(id: T, version: Version, events: List<DomainEvent> = emptyList()) :
    DomainEntity<T>(id, version, events)