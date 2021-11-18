project.base.archivesName.set("shop-postgres-persistence")

plugins {
    id(Plugins.spring_kotlin) version PluginVers.spring_kotlin
}

dependencies {
    // project
    implementation(project(":common:types"))
    implementation(project(":common:events"))
    implementation(project(":shop:domain"))
    implementation(project(":shop:usecase"))

    // kotlin
    implementation(Libs.kotlin_jdk8)
    implementation(Libs.kotlin_reflect)
    implementation(Libs.kotlin_stdlib)
    implementation(Libs.arrow)

    // database
    implementation(Libs.spring_boot_starter_jdbc)
    implementation(Libs.liquibase)
    implementation(Libs.postgresql)

    // test
    testImplementation(Libs.kotest_junit)
    testImplementation(Libs.kotest_arrow)
    testImplementation(Libs.junit_engine)
    testImplementation(Libs.junit_params)
    testImplementation(Libs.testcontainers)

    // test
    testImplementation(Libs.spring_boot_starter_test) {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    testImplementation(testFixtures(project(":common:types")))
    testImplementation(testFixtures(project(":shop:domain")))

    testFixturesImplementation(testFixtures(project(":common:types")))
    testFixturesImplementation(testFixtures(project(":common:events")))
    testFixturesImplementation(testFixtures(project(":shop:domain")))
    testFixturesImplementation(Libs.testcontainers)
    testFixturesImplementation(Libs.spring_boot_starter_jdbc)
    testFixturesImplementation(Libs.arrow)
    testFixturesImplementation(Libs.liquibase)
}