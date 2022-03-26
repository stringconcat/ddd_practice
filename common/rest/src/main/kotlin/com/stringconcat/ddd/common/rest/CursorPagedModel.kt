package com.stringconcat.ddd.common.rest

data class CursorPagedModel<T, ID>(val list: List<T>, val next: ID? = null) {
    val count = list.size
}