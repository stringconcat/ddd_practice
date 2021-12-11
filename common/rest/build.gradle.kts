project.base.archivesName.set("common-rest")

plugins {
    id(Plugins.spring_kotlin) version PluginVers.spring_kotlin
}

dependencies {
    // kotlin
    implementation(Libs.kotlin_jdk8)
    implementation(Libs.arrow)

    // spring
    implementation(Libs.spring_boot_starter_web)
    implementation(Libs.spring_boot_started_hateoas)

    // jackson
    implementation(Libs.jackson_kotlin)

    // tests

    testImplementation(Libs.kotest_junit)
    testImplementation(Libs.spring_boot_starter_test) {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation(Libs.spring_boot_starter_web)
    testImplementation(Libs.spring_boot_started_hateoas)

    testFixturesImplementation(Libs.spring_boot_starter_test) {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testFixturesImplementation(Libs.spring_boot_starter_web)
}
