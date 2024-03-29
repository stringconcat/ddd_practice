project.base.archivesName.set("shop-usecase")

dependencies {
    // project
    implementation(project(":shop:domain"))
    implementation(project(":common:types"))
    implementation(project(":common:events"))

    // logging
    implementation(Libs.slf4j_api)

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

    testImplementation(testFixtures(project(":common:types")))
    testImplementation(testFixtures(project(":shop:domain")))

    testFixturesImplementation(testFixtures(project(":common:types")))
    testFixturesImplementation(testFixtures(project(":shop:domain")))
    testFixturesImplementation(Libs.arrow)
    testFixturesImplementation(Libs.kotest_junit)
}