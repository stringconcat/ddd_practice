dependencies {
    // project
    implementation(project(":common:types"))
    implementation(project(":order:domain"))
    implementation(project(":order:usecase"))

    // kotlin
    implementation(Libs.kotlin_jdk8)
    implementation(Libs.kotlin_reflect)
    implementation(Libs.kotlin_stdlib)
    implementation(Libs.arrow)

    // spring
    implementation(Libs.spring_boot_starter_web)
    implementation(Libs.spring_boot_starter_thymeleaf)

    // test
    testImplementation(Libs.kotest_junit)
    testImplementation(Libs.kotest_arrow)
    testImplementation(Libs.junit_engine)
    testImplementation(Libs.junit_params)
}