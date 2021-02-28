package com.stringconcat.dev.course.app.controllers

object URLs {
    const val rootMenu = "/menu"
    const val listMenu = rootMenu
    const val addMeal = "$rootMenu/add"
    const val removeMeal = "$rootMenu/remove"
    const val payment = "/payment"
}

object Views {
    const val menu = "menu"
    const val payment = "payment"
    const val paymentResult = "payment_result"
}