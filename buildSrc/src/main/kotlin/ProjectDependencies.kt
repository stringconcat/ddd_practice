object LibVers {

    const val spring_boot = "2.4.3"
    const val junit = "5.7.1"
    const val kotest = "4.4.1"
    const val jackson = "2.12.1"
    const val arrow = "0.11.0"
    const val log4j = "2.12.0"
    const val telnet = "1.1"

}

object Libs {

    // Kotlin
    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Global.kotlin_version}"
    const val kotlin_jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Global.kotlin_version}"
    const val kotlin_reflect = "org.jetbrains.kotlin:kotlin-reflect:${Global.kotlin_version}"
    const val arrow = "io.arrow-kt:arrow-core:${LibVers.arrow}"

    // Jackson
    const val jackson_kotlin = "com.fasterxml.jackson.module:jackson-module-kotlin:${LibVers.jackson}"

    // Spring
    const val spring_boot_starter_web = "org.springframework.boot:spring-boot-starter-web:${LibVers.spring_boot}"
    const val spring_boot_starter_test = "org.springframework.boot:spring-boot-starter-test:${LibVers.spring_boot}"
    const val spring_boot_starter_thymeleaf =
        "org.springframework.boot:spring-boot-starter-thymeleaf:${LibVers.spring_boot}"

    // Logging
    const val log4j = "org.apache.logging.log4j:log4j-api:${LibVers.log4j}"
    const val log4j_core = "org.apache.logging.log4j:log4j-core:${LibVers.log4j}"
    const val log4j_kotlin = "org.apache.logging.log4j:log4j-api-kotlin:1.0.0"
    const val slf4j_over_log4j = "org.apache.logging.log4j:log4j-slf4j-impl:${LibVers.log4j}"

    // Tests
    const val junit_params = "org.junit.jupiter:junit-jupiter-params:${LibVers.junit}"
    const val junit_engine = "org.junit.jupiter:junit-jupiter-engine:${LibVers.junit}"
    const val kotest_junit = "io.kotest:kotest-runner-junit5:${LibVers.kotest}"
    const val kotest_arrow = "io.kotest:kotest-assertions-arrow:${LibVers.kotest}"

    // telnet
    const val telnet = "com.khubla.ktelnet:ktelnet:${LibVers.telnet}"
}

object PluginVers {
    const val kotlin = Global.kotlin_version
    const val spring_boot = LibVers.spring_boot
    const val detekt = "1.15.0"
    const val detekt_formatting = detekt
    const val spring_dependency_management = "1.0.11.RELEASE"
    const val spring_kotlin = Global.kotlin_version
    const val update_dependencies = "0.36.0"
    const val owasp_dependencies = "6.1.0"
}

object Plugins {
    const val kotlin = "org.jetbrains.kotlin.jvm"
    const val spring_boot = "org.springframework.boot"
    const val detekt = "io.gitlab.arturbosch.detekt"
    const val detekt_formatting = "io.gitlab.arturbosch.detekt:detekt-formatting"
    const val spring_dependency_management = "io.spring.dependency-management"
    const val spring_kotlin = "org.jetbrains.kotlin.plugin.spring"
    const val update_dependencies = "com.github.ben-manes.versions"
    const val owasp_dependencies = "org.owasp.dependencycheck"
}
