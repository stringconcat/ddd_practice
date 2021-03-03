package com.stringconcat.dev.course.app.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class IndexController {

    @GetMapping
    fun index() = "redirect:${URLs.rootMenu}"
}