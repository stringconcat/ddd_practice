project.base.archivesName.set("kitchen-application")
plugins {
    id(Plugins.spring_boot) version PluginVers.spring_boot
    id(Plugins.spring_dependency_management) version PluginVers.spring_dependency_management
    id(Plugins.spring_kotlin) version PluginVers.spring_kotlin
}

dependencies {

    // project
    implementation(project(":common:types"))
    implementation(project(":common:events"))
    implementation(project(":common:rest"))

    implementation(project(":kitchen:domain"))
    implementation(project(":kitchen:usecase"))
    implementation(project(":kitchen:persistence"))
    implementation(project(":kitchen:rest"))

    // kotlin
    implementation(Libs.kotlin_jdk8)
    implementation(Libs.kotlin_reflect)
    implementation(Libs.kotlin_stdlib)

    // spring
    implementation(Libs.spring_boot_starter_web)

    implementation(Libs.spring_boot_starter_logging)
    implementation(Libs.spring_boot_started_hateoas)

    // swagger
    implementation(Libs.swagger)
    implementation(Libs.swagger_ui)

    // jackson
    implementation(Libs.jackson_kotlin)

    // arrow
    implementation(Libs.arrow)

    // logging
    implementation(Libs.slf4j_api)

    // test
    testImplementation(Libs.kotest_junit)
    testImplementation(Libs.kotest_arrow)
    testImplementation(Libs.junit_engine)
    testImplementation(Libs.junit_params)
    testImplementation(Libs.spring_boot_starter_test) {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation(Libs.arch_unit)
    testImplementation(Libs.commons_net)

    testImplementation(testFixtures(project(":common:types")))
    testFixturesImplementation(testFixtures(project(":common:types")))
    testFixturesImplementation(project(":common:events"))
    testFixturesImplementation(Libs.arrow)
    testFixturesImplementation(Libs.kotest_junit)
    testFixturesImplementation(Libs.commons_net)
    testFixturesImplementation(Libs.spring_boot_starter_test) {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testFixturesImplementation(Libs.spring_boot_starter_web)
}