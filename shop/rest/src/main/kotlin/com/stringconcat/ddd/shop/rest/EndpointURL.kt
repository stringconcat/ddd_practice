package com.stringconcat.ddd.shop.rest

const val API_V1 = "/rest/shop/v1"
const val API_V1_MENU = "$API_V1/menu"
const val API_V1_ORDER = "$API_V1/orders"

const val API_V1_MENU_ADD_TO_MENU = API_V1_MENU
const val API_V1_MENU_GET_BY_ID = "$API_V1_MENU/{id}"
const val API_V1_MENU_GET_ALL = API_V1_MENU
const val API_V1_MENU_DELETE_BY_ID = "$API_V1_MENU/{id}"

const val API_V1_ORDER_CANCEL_BY_ID = "$API_V1_ORDER/{id}/cancel"
const val API_V1_ORDER_CONFIRM_BY_ID = "$API_V1_ORDER/{id}/confirm"
const val API_V1_ORDER_GET_BY_ID = "$API_V1_ORDER/{id}"
const val API_V1_ORDER_GET_ALL = API_V1_ORDER
const val API_V1_ORDER_GET_WITH_PAGINATION = "$API_V1_ORDER_GET_ALL?startId={startId}&limit={limit}"