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

    // jackson
    implementation(Libs.jackson_kotlin)

    // logging
    implementation(Libs.slf4j_api)

    // test
    testImplementation(Libs.kotest_junit)
    testImplementation(Libs.junit_engine)
    testImplementation(Libs.junit_params)
    testImplementation(Libs.pact_provider)
    testImplementation(Libs.spring_boot_starter_test) {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.build {
    dependsOn("bootJar")
}

tasks.register<Copy>("copyPacts") {
    val srcDir = "${rootProject.buildDir}/pacts"
    val destDir = "${project.buildDir}/pacts"
    from(layout.buildDirectory.dir(srcDir))
    include("*.json")
    into(layout.buildDirectory.dir(destDir))
}

tasks.getByName("test") {
    dependsOn("copyPacts")
}