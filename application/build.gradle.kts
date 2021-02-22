plugins {
    id(Plugins.spring_boot) version PluginVers.spring_boot
    id(Plugins.spring_dependency_management) version PluginVers.spring_dependency_management
    id(Plugins.spring_kotlin) version PluginVers.spring_kotlin
}

dependencies {

    // project
    implementation(project(":common:types"))

    implementation(project(":kitchen:domain"))
    implementation(project(":kitchen:usecase"))
    implementation(project(":kitchen:persistence"))

    implementation(project(":order:domain"))
    implementation(project(":order:usecase"))
    implementation(project(":order:persistence"))

    implementation(project(":integration:payment"))

    // kotlin
    implementation(Libs.kotlin_jdk8)
    implementation(Libs.kotlin_reflect)
    implementation(Libs.kotlin_stdlib)

    // spring
    implementation(Libs.spring_boot_starter_web)

    // jackson
    implementation(Libs.jackson_kotlin)

    // arrow
    implementation(Libs.arrow)

    // logging
    implementation(Libs.log4j_kotlin)
    implementation(Libs.log4j_core)

    // test
    testImplementation(Libs.kotest_junit)
    testImplementation(Libs.kotest_arrow)
    testImplementation(Libs.junit_engine)
    testImplementation(Libs.junit_params)
    testImplementation(Libs.spring_boot_starter_test) {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}