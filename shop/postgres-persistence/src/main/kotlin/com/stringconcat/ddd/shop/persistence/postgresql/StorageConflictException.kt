package com.stringconcat.ddd.shop.persistence.postgresql

class StorageConflictException(override val message: String? = null, cause: Throwable? = null) :
    RuntimeException(message, cause)