object LibVers {

    const val spring_boot = "2.4.2"
    const val junit = "5.2.0"
    const val sl4j = "1.7.25"
    const val kotlintest = "3.4.2"
    const val jackson = "2.9.9"

}

object Libs {

    //Kotlin
    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Global.kotlin}"
    const val kotlin_jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Global.kotlin}"
    const val kotlin_reflect = "org.jetbrains.kotlin:kotlin-reflect:${Global.kotlin}"


    //Jackson
    const val jackson_kotlin = "com.fasterxml.jackson.module:jackson-module-kotlin:${LibVers.jackson}"

    //Spring
    const val spring_boot_starter_web = "org.springframework.boot:spring-boot-starter-web:${LibVers.spring_boot}"
    const val spring_boot_starter_test = "org.springframework.boot:spring-boot-starter-test:${LibVers.spring_boot}"

    // Logging
    const val slf4j_api = "org.slf4j:slf4j-api:${LibVers.sl4j}"
    const val slf4j_simple = "org.slf4j:slf4j-simple:${LibVers.sl4j}"
    const val kotlin_logging = "io.github.microutils:kotlin-logging:1.4.9"

    // Tests
    const val junit_api = "org.junit.jupiter:junit-jupiter-api:${LibVers.junit}"
    const val junit_params = "org.junit.jupiter:junit-jupiter-params:${LibVers.junit}"
    const val junit_engine = "org.junit.jupiter:junit-jupiter-engine:${LibVers.junit}"
    const val kotlintest = "io.kotlintest:kotlintest-runner-junit5:${LibVers.kotlintest}"

}

object PluginVers {
    const val kotlin = Global.kotlin
    const val spring_boot = LibVers.spring_boot
    const val detekt = "1.15.0"
    const val detekt_formatting = detekt
    const val spring_dependency_management = "1.0.11.RELEASE"
    const val spring_kotlin = Global.kotlin
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