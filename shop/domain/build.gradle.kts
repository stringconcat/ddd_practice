project.base.archivesName.set("shop-domain")

dependencies {
    // project
    implementation(project(":common:types"))

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

    testFixturesImplementation(testFixtures(project(":common:types")))
    testFixturesImplementation(Libs.arrow)
    testFixturesImplementation(Libs.faker)
}