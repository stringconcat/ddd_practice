package com.stringconcat.ddd.shop.rest.menu

import com.stringconcat.ddd.shop.usecase.menu.GetMenu
import com.stringconcat.ddd.shop.usecase.menu.dto.MealInfo
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import java.math.BigDecimal
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(tags = ["Menu"])
class GetMenuEndpoint(private val getMenu: GetMenu) {

    @ApiOperation("Get menu")
    @GetMapping(path = ["/rest/v1/menu"])
    fun execute(): ResponseEntity<CollectionModel<MealModel>> {
        val menuModel = getMenu.execute().map { MealModel.from(it) }
        val collectionModel = CollectionModel.of(menuModel)
            .add(linkTo(methodOn(GetMenuEndpoint::class.java).execute()).withSelfRel())
        return ResponseEntity.ok(collectionModel)
    }
}

@Relation(collectionRelation = "meals")
data class MealModel(
    val id: Long,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val version: Long,
) : RepresentationModel<MealModel>() {

    companion object {
        fun from(mealInfo: MealInfo): MealModel =
            MealModel(id = mealInfo.id.value,
                name = mealInfo.name.value,
                price = mealInfo.price.value,
                description = mealInfo.description.value,
                version = mealInfo.version.value)
    }
}
