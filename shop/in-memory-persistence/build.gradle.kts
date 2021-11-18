project.base.archivesName.set("shop-in-memory-persistence")

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

    // test
    testImplementation(Libs.kotest_junit)
    testImplementation(Libs.kotest_arrow)
    testImplementation(Libs.junit_engine)
    testImplementation(Libs.junit_params)

    testImplementation(testFixtures(project(":common:types")))
    testImplementation(testFixtures(project(":shop:domain")))

    testFixturesImplementation(testFixtures(project(":common:types")))
    testFixturesImplementation(testFixtures(project(":common:events")))
    testFixturesImplementation(testFixtures(project(":shop:domain")))
    testFixturesImplementation(Libs.arrow)
}