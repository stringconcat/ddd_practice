package com.stringconcat.ddd.tests.common

import java.io.File
import java.io.FileInputStream
import java.util.Properties

class StandConfiguration {

    companion object {
        const val DOCKER_COMPOSE_FILE_PROPERTY = "dockerComposeFile"
        const val ENV_FILE_PROPERTY = "envFile"
        const val START_DOCKER_PROPERTY = "startDocker"
        const val HOST_TEMPLATE = "http://localhost:"
        const val SHOP_PORT_PROPERTY = "SHOP_PORT"
        const val SHOP_TELNET_PORT_PROPERTY = "SHOP_TELNET_PORT"
        const val KITCHEN_PORT_PROPERTY = "KITCHEN_PORT"
        const val CRM_PORT_PROPERTY = "MOCK_PORT"
    }

    val shopBaseUrl: String
    val shopTelnetPort: Int
    val kitchenBaseUrl: String
    val crmBaseUrl: String
    val dockerCompose: File
    val dockerComposeEnv: Map<String, String>
    val startDocker: Boolean

    init {
        val dockerComposeProperty = requireNotNull(System.getProperty(DOCKER_COMPOSE_FILE_PROPERTY)) {
            "Property $DOCKER_COMPOSE_FILE_PROPERTY is null"
        }
        dockerCompose = File(dockerComposeProperty)

        val envProperty = requireNotNull(System.getProperty(ENV_FILE_PROPERTY)) {
            "Property $ENV_FILE_PROPERTY is null"
        }

        startDocker = requireNotNull(System.getProperty(START_DOCKER_PROPERTY)) {
            "Property $START_DOCKER_PROPERTY is null"
        }.toBoolean()

        val env = File(envProperty)
        val props = Properties()
        props.load(FileInputStream(env))

        shopBaseUrl = HOST_TEMPLATE + props.property(SHOP_PORT_PROPERTY)
        kitchenBaseUrl = HOST_TEMPLATE + props.property(KITCHEN_PORT_PROPERTY)
        crmBaseUrl = HOST_TEMPLATE + props.property(CRM_PORT_PROPERTY)
        shopTelnetPort = props.property(SHOP_TELNET_PORT_PROPERTY).toInt()
        dockerComposeEnv = props.entries.associate { it.key.toString() to it.value.toString() }
    }

    private fun Properties.property(name: String) =
        requireNotNull(getProperty(name)) { "Property $name must not be null" }

    override fun toString(): String {
        return "StandConfiguration(shopBaseUrl='$shopBaseUrl', \n" +
                "shopTelnetPort=$shopTelnetPort, \n" +
                "kitchenBaseUrl='$kitchenBaseUrl', \n" +
                "crmBaseUrl='$crmBaseUrl', \n" +
                "dockerCompose=$dockerCompose, \n" +
                "dockerComposeEnv=$dockerComposeEnv, \n" +
                "startDocker=$startDocker)"
    }
}