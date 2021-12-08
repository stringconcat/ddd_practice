package com.stringconcat.ddd.shop.app

import com.stringconcat.ddd.shop.persistence.postgresql.StorageConflictException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ErrorController {

    @GetMapping("storageConflict")
    fun storageConflict(): Nothing = throw StorageConflictException("OOoops!")
}