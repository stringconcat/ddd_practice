package com.stringconcat.ddd.kitchen.rest

const val API_V1 = "/rest/kitchen/v1"
const val API_V1_ORDERS = "$API_V1/orders"

const val API_V1_ORDERS_COOK = "$API_V1_ORDERS/{id}/cook"
const val API_V1_ORDERS_GET_BY_ID = "$API_V1_ORDERS/{id}"
const val API_V1_ORDERS_GET_ALL = API_V1_ORDERS