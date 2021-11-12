package com.stringconcat.ddd.shop.rest.order

import org.springframework.hateoas.PagedModel
import org.springframework.web.bind.annotation.RestController

@RestController
class GetOrdersEndpoint {

    fun execute() {
        PagedModel.of("")
    }
}