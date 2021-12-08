package com.stringconcat.ddd.kitchen.app.controllers

import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexEndpoint {

    @GetMapping
    fun execute(): ResponseEntity<IndexModel> =
        ResponseEntity.ok(
            IndexModel()
                .add(linkTo(methodOn(IndexEndpoint::class.java).execute()).withSelfRel())
                .add(
                    linkTo(
                        methodOn(com.stringconcat.ddd.kitchen.rest.order.GetOrdersEndpoint::class.java)
                            .execute()
                    )
                        .withRel("kitchen")
                )
        )
}

data class IndexModel(val title: String = "Restaurant administration panel") : RepresentationModel<IndexModel>()