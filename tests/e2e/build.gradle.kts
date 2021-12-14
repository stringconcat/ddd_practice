plugins {
    id(Plugins.allure) version PluginVers.allure
}

allure {
    adapter {
        version.set(PluginVers.allure_cli)
        allureJavaVersion.set(PluginVers.allure_java)

        frameworks {
            junit5 {
                enabled.set(false)
            }
        }
    }
}

configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}

dependencies {

    // kotlin
    implementation(Libs.kotlin_jdk8)
    implementation(Libs.kotlin_reflect)
    implementation(Libs.kotlin_stdlib)
    implementation(Libs.arrow)

    // test
    testImplementation(Libs.kotest_junit)
    testImplementation(Libs.kotest_arrow)
    testImplementation(Libs.junit_engine)
    testImplementation(Libs.junit_params)
    testImplementation(Libs.testcontainers_core)

    testImplementation(Libs.kbdd)
    testImplementation(Libs.rest_assured)
    testImplementation(Libs.jackson_kotlin)
    testImplementation(Libs.jackson_databind)
    testImplementation(Libs.jfix_corounit_allure)
    testImplementation(Libs.jfix_corounit_engine)
    testImplementation(Libs.koin)
    testImplementation(Libs.commons_net)
    testImplementation(testFixtures(project(":shop:application")))

    testFixturesImplementation(Libs.faker)
    testFixturesImplementation(testFixtures(project(":shop:application")))
    testFixturesImplementation(Libs.kbdd)
    testFixturesImplementation(Libs.commons_net)
}

tasks.withType<Test> {
    systemProperty("dockerComposeFile", "${project.rootProject.rootDir}/docker/docker-compose.yml")
    systemProperty("envFile", "${project.rootProject.rootDir}/docker/env/e2e.env")
}