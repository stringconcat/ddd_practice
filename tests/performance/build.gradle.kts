plugins {
    id(Plugins.gatling) version PluginVers.gatling
}

dependencies {

    // kotlin
    implementation(Libs.kotlin_jdk8)
    implementation(Libs.kotlin_reflect)
    implementation(Libs.kotlin_stdlib)

    // test
    testImplementation(Libs.kotest_junit)
    testImplementation(Libs.junit_engine)
    testImplementation(Libs.junit_params)
    testImplementation(Libs.testcontainers_core)
    testImplementation(Libs.rest_assured)
    testImplementation(Libs.jackson_kotlin)
    testImplementation(Libs.jackson_databind)
    testImplementation(Libs.commons_net)
    testFixturesImplementation(Libs.faker)
}

tasks.withType<Test> {
    val dockerComposeFileProperty = "dockerComposeFile"
    val envFileProperty = "envFile"
    val startDockerProperty = "startDocker"

    val dockerComposeFile = System.getProperty(dockerComposeFileProperty,
        "${project.rootProject.rootDir}/docker/docker-compose.yml")
    systemProperty(dockerComposeFileProperty, dockerComposeFile)

    val envFile = System.getProperty(envFileProperty,
        "${project.rootProject.rootDir}/docker/env/performance.env")
    systemProperty(envFileProperty, envFile)

    val startDocker = System.getProperty(startDockerProperty, "true")
    systemProperty(startDockerProperty, startDocker)
}