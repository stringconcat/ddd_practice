project.base.archivesName.set("kitchen-usecase")

dependencies {
    // project
    implementation(project(":kitchen:domain"))
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
    testImplementation(testFixtures(project(":kitchen:domain")))

    testFixturesImplementation(testFixtures(project(":common:types")))
    testFixturesImplementation(testFixtures(project(":kitchen:domain")))
    testFixturesImplementation(Libs.arrow)
}