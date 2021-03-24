package com.stringconcat.dev.course.app.controllers

object URLs {
    const val rootMenu = "/menu"
    const val listMenu = rootMenu
    const val addMeal = "$rootMenu/add"
    const val removeMeal = "$rootMenu/remove"

    const val payment = "/payment"

    const val shop_orders = "/shop/orders"
    const val confirm_shop_order = "$shop_orders/confirm"
    const val cancel_shop_order = "$shop_orders/cancel"

    const val kitchen_orders = "/kitchen/orders"
    const val cook_kitchen_order = "$kitchen_orders/cook"
}

object Views {
    const val menu = "menu"
    const val payment = "payment"
    const val paymentResult = "payment_result"

    const val shopOrders = "shop_orders"

    const val kitchenOrders = "kitchen_orders"
}