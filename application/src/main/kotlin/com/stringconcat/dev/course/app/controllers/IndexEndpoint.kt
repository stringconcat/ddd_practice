package com.stringconcat.dev.course.app.controllers

import com.stringconcat.ddd.shop.rest.menu.GetMenuEndpoint
import com.stringconcat.ddd.shop.rest.order.GetOrdersEndpoint
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
                .add(linkTo(methodOn(GetMenuEndpoint::class.java).execute()).withRel("menu"))
                .add(linkTo(methodOn(GetOrdersEndpoint::class.java)
                    .execute(
                        startId = 0,
                        limit = 10)
                ).withRel("orders")))
}

data class IndexModel(val title: String = "Restaurant administration panel22") : RepresentationModel<IndexModel>()