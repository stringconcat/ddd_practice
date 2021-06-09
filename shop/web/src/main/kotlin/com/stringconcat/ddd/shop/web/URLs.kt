package com.stringconcat.ddd.shop.web

object URLs {
    const val listMenu = "/menu"
    const val addMeal = "$listMenu/add"
    const val removeMeal = "$listMenu/remove"

    const val shop_orders = "/shop/orders"
    const val confirm_shop_order = "$shop_orders/confirm"
    const val cancel_shop_order = "$shop_orders/cancel"
}

object Views {
    const val menu = "menu"
    const val shopOrders = "shop_orders"
}