package com.stringconcat.ddd.shop.app.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexEndpoint {

    @GetMapping
    fun execute(): ResponseEntity<IndexModel> =
        ResponseEntity.ok(IndexModel())
}

data class IndexModel(val title: String = "Restaurant administration panel22")