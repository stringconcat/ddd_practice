package com.stringconcat.ddd.shop.app.controllers

import com.stringconcat.ddd.common.rest.GlobalErrorHandler
import com.stringconcat.ddd.shop.persistence.postgresql.StorageConflictException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler

class ShopErrorHandler : GlobalErrorHandler() {

    @ExceptionHandler(value = [StorageConflictException::class])
    fun handleStorageConflict(ex: StorageConflictException) =
        logErrorAndBuildResponse(
            throwable = ex,
            status = HttpStatus.CONFLICT,
            title = "Conflict",
            code = "conflict"
        )
}