package com.stringconcat.ddd.tests.common

import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait

class StandContainer(private val configuration: StandConfiguration) {

    lateinit var dockerComposeContainer: DockerComposeContainer<Nothing>

    fun start() {
        if (configuration.startDocker) {
            println("\nStarting stand with settings:\n$configuration\n")
            dockerComposeContainer = DockerComposeContainer<Nothing>(configuration.dockerCompose).apply {
                waitingFor("shop",
                    Wait.forLogMessage(".*Started ShopApplicationKt in.*", 1))
                waitingFor("kitchen",
                    Wait.forLogMessage(".*Started KitchenApplicationKt in.*", 1))
                withEnv(configuration.dockerComposeEnv)
                start()
            }
        } else {
            println("Docker disabled")
        }
    }

    fun stop() {
        if (configuration.startDocker) {
            dockerComposeContainer.stop()
        }
    }
}