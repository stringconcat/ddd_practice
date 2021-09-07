dependencies {
    // project
    implementation(project(":common:types"))
    implementation(project(":shop:domain"))
    implementation(project(":shop:usecase"))
    implementation(project(":common:telnet"))

    // kotlin
    implementation(Libs.kotlin_jdk8)
    implementation(Libs.kotlin_reflect)
    implementation(Libs.kotlin_stdlib)
    implementation(Libs.arrow)

    // table
    implementation(Libs.ascii_table)

    // test
    testImplementation(Libs.kotest_junit)
    testImplementation(Libs.kotest_arrow)
    testImplementation(Libs.junit_engine)
    testImplementation(Libs.junit_params)
}