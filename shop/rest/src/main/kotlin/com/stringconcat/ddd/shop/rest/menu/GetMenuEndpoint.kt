package com.stringconcat.ddd.shop.rest.menu

import com.stringconcat.ddd.shop.rest.API_V1_MENU
import com.stringconcat.ddd.shop.rest.menu.views.MealModel
import com.stringconcat.ddd.shop.usecase.menu.GetMenu
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(tags = ["Menu"])
class GetMenuEndpoint(private val getMenu: GetMenu) {

    @ApiOperation("Get menu")
    @GetMapping(path = [API_V1_MENU])
    fun execute(): ResponseEntity<CollectionModel<MealModel>> {
        val menuModel = getMenu.execute().map { MealModel.from(it) }
        val collectionModel = CollectionModel.of(menuModel)
            .add(linkTo(methodOn(GetMenuEndpoint::class.java).execute()).withSelfRel())
        return ResponseEntity.ok(collectionModel)
    }
}