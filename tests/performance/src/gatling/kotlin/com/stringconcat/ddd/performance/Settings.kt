package com.stringconcat.ddd.performance

import kotlin.time.Duration


class Settings {
    companion object {
        const val USER_PER_SEC_PROPERTY = "usersPerSec"
        const val DURATION_PROPERTY = "duration"
        const val MENU_SIZE_PROPERTY = "menuSize"
    }

    val usersPerSec = requireNotNull(System.getProperty(USER_PER_SEC_PROPERTY)) {
        "Property $USER_PER_SEC_PROPERTY is null"
    }.toDouble()

    val menuSize = requireNotNull(System.getProperty(MENU_SIZE_PROPERTY)) {
        "Property $MENU_SIZE_PROPERTY is null"
    }.toInt()

    val duration = Duration.parse(requireNotNull(System.getProperty(DURATION_PROPERTY)) {
        "Property $DURATION_PROPERTY is null"
    })

    override fun toString(): String {
        return "Settings(usersPerSec=$usersPerSec, \n" +
                "menuSize=$menuSize, \n" +
                "duration=$duration)"
    }

}