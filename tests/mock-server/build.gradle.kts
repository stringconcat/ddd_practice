plugins {
    id(Plugins.spring_boot) version PluginVers.spring_boot
    id(Plugins.spring_dependency_management) version PluginVers.spring_dependency_management
    id(Plugins.spring_kotlin) version PluginVers.spring_kotlin
}

dependencies {

    // kotlin
    implementation(Libs.kotlin_jdk8)
    implementation(Libs.kotlin_reflect)
    implementation(Libs.kotlin_stdlib)

    // spring
    implementation(Libs.spring_boot_starter_web)

    // swagger
    implementation(Libs.swagger)
    implementation(Libs.swagger_ui)

    // jackson
    implementation(Libs.jackson_kotlin)

    // logging
    implementation(Libs.slf4j_api)

    // test
    testImplementation(Libs.kotest_junit)
    testImplementation(Libs.junit_engine)
    testImplementation(Libs.junit_params)
    testImplementation(Libs.spring_boot_starter_test) {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}