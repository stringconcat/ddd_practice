project.base.archivesName.set("shop-application")

plugins {
    id(Plugins.spring_boot) version PluginVers.spring_boot
    id(Plugins.spring_dependency_management) version PluginVers.spring_dependency_management
    id(Plugins.spring_kotlin) version PluginVers.spring_kotlin
}

dependencies {

    // project
    implementation(project(":common:types"))
    implementation(project(":common:telnet"))
    implementation(project(":common:events"))
    implementation(project(":common:rest"))

    implementation(project(":shop:domain"))
    implementation(project(":shop:usecase"))
    implementation(project(":shop:in-memory-persistence"))
    implementation(project(":shop:postgres-persistence"))
    implementation(project(":shop:telnet"))
    implementation(project(":shop:payment"))
    implementation(project(":shop:crm"))
    implementation(project(":shop:rest"))

    // kotlin
    implementation(Libs.kotlin_jdk8)
    implementation(Libs.kotlin_reflect)
    implementation(Libs.kotlin_stdlib)

    // spring
    implementation(Libs.spring_boot_starter_web)
    implementation(Libs.spring_boot_starter_thymeleaf)
    implementation(Libs.spring_boot_starter_logging)
    implementation(Libs.spring_boot_started_hateoas)
    implementation(Libs.spring_boot_starter_jdbc)
    implementation(Libs.spring_boot_starter_amqp)

    // swagger
    implementation(Libs.swagger)
    implementation(Libs.swagger_ui)

    // jackson
    implementation(Libs.jackson_kotlin)

    // arrow
    implementation(Libs.arrow)

    // table
    implementation(Libs.ascii_table)

    // logging
    implementation(Libs.slf4j_api)

    // databse
    implementation(Libs.postgresql)

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
    testImplementation(Libs.testcontainers_rabbit)

    testImplementation(testFixtures(project(":common:types")))
    testImplementation(testFixtures(project(":shop:domain")))
    testImplementation(testFixtures(project(":shop:usecase")))

    testFixturesImplementation(testFixtures(project(":common:types")))
    testFixturesImplementation(testFixtures(project(":shop:domain")))
    testFixturesImplementation(testFixtures(project(":shop:usecase")))
    testFixturesImplementation(testFixtures(project(":shop:payment")))
    testFixturesImplementation(project(":shop:in-memory-persistence"))
    testFixturesImplementation(project(":shop:postgres-persistence"))
    testFixturesImplementation(project(":common:events"))
    testFixturesImplementation(Libs.arrow)
    testFixturesImplementation(Libs.kotest_junit)
    testFixturesImplementation(Libs.commons_net)
    testFixturesImplementation(Libs.spring_boot_starter_test) {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testFixturesImplementation(Libs.spring_boot_starter_web)
}