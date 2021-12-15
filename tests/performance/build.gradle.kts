plugins {
    id(Plugins.gatling) version PluginVers.gatling
}

dependencies {
    // kotlin
    implementation(Libs.kotlin_jdk8)
    implementation(Libs.kotlin_reflect)
    implementation(Libs.kotlin_stdlib)

    // test
    gatlingImplementation(project(":tests:common"))
    gatlingImplementation(Libs.testcontainers_core)
    gatlingImplementation(Libs.rest_assured)
    gatlingImplementation(Libs.rest_assured_kotlin)
    gatlingImplementation(Libs.faker)
}

gatling {

    val dockerComposeFileProperty = "dockerComposeFile"
    val envFileProperty = "envFile"
    val startDockerProperty = "startDocker"
    val usersPerSecPropery = "usersPerSec"
    val durationProperty = "duration"
    val menuSizeProperty = "menuSize"

    val dockerComposeFile = System.getProperty(dockerComposeFileProperty,
        "${project.rootProject.rootDir}/docker/docker-compose.yml")
    systemProperties[dockerComposeFileProperty] = dockerComposeFile

    val envFile = System.getProperty(envFileProperty,
        "${project.rootProject.rootDir}/docker/env/performance.env")
    systemProperties[envFileProperty] = envFile

    val startDocker = System.getProperty(startDockerProperty, "true")
    systemProperties[startDockerProperty] = startDocker

    val usersPerSec = System.getProperty(usersPerSecPropery, "10.0")
    systemProperties[usersPerSecPropery] = usersPerSec

    val duration = System.getProperty(durationProperty, "PT10S")
    systemProperties[durationProperty] = duration

    val menuSize = System.getProperty(menuSizeProperty, "20")
    systemProperties[menuSizeProperty] = menuSize
}