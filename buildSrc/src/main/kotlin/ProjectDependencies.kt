object LibVers {
    const val spring_boot = "2.6.1"
    const val swagger = "3.0.0"
    const val junit = "5.7.1"
    const val kotest = "5.0.2"
    const val kotest_arrow = "1.2.0"
    const val jackson = "2.13.0"
    const val arrow = "1.0.1"
    const val telnet = "1.1"
    const val slf4j = "1.7.32"
    const val ascii_table = "1.2.0"
    const val postgresql = "42.3.1"
    const val liquibase = "4.6.1"
    const val testng = "7.3.0"
    const val commons_net = "3.8.0"
    const val arch_unit = "0.22.0"
    const val faker = "1.0.2"
    const val testcontainers = "1.16.2"
    const val pact = "4.3.2"
    const val wiremock = "2.31.0"
    const val resilience4j_circuitbreaker = "1.7.1"
    const val resilience4j_bulkhead = "1.7.1"
    const val kbdd = "1.1.1"
    const val koin = "2.0.1"
    const val rest_assured = "4.4.0"
    const val corounit = "1.1.1"
    const val spring_rabbit_test = "2.4.0"
    const val hal_explorer = "1.0.1"
}

object Libs {
    // Kotlin
    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Global.kotlin_version}"
    const val kotlin_jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Global.kotlin_version}"
    const val kotlin_reflect = "org.jetbrains.kotlin:kotlin-reflect:${Global.kotlin_version}"
    const val arrow = "io.arrow-kt:arrow-core:${LibVers.arrow}"

    // Jackson
    const val jackson_kotlin = "com.fasterxml.jackson.module:jackson-module-kotlin:${LibVers.jackson}"
    const val jackson_databind = "com.fasterxml.jackson.core:jackson-databind:${LibVers.jackson}"

    // Spring
    const val spring_boot_starter_web = "org.springframework.boot:spring-boot-starter-web:${LibVers.spring_boot}"
    const val spring_boot_starter_jdbc = "org.springframework.boot:spring-boot-starter-jdbc:${LibVers.spring_boot}"
    const val spring_boot_starter_logging = "org.springframework.boot:spring-boot-starter-logging:${
        LibVers.spring_boot
    }"
    const val spring_boot_starter_test = "org.springframework.boot:spring-boot-starter-test:${LibVers.spring_boot}"
    const val spring_boot_starter_thymeleaf =
        "org.springframework.boot:spring-boot-starter-thymeleaf:${LibVers.spring_boot}"

    const val spring_boot_started_hateoas =
        "org.springframework.boot:spring-boot-starter-hateoas:${LibVers.spring_boot}"

    const val spring_boot_starter_amqp =
        "org.springframework.boot:spring-boot-starter-amqp:${LibVers.spring_boot}"

    const val swagger = "io.springfox:springfox-boot-starter:${LibVers.swagger}"
    const val swagger_ui = "io.springfox:springfox-swagger-ui:${LibVers.swagger}"

    // Logging
    const val slf4j_api = "org.slf4j:slf4j-api:${LibVers.slf4j}"

    // Table
    const val ascii_table = "com.github.freva:ascii-table:${LibVers.ascii_table}"

    // Tests
    const val junit_params = "org.junit.jupiter:junit-jupiter-params:${LibVers.junit}"
    const val junit_engine = "org.junit.jupiter:junit-jupiter-engine:${LibVers.junit}"
    const val kotest_junit = "io.kotest:kotest-runner-junit5:${LibVers.kotest}"
    const val kotest_arrow = "io.kotest.extensions:kotest-assertions-arrow-jvm:${LibVers.kotest_arrow}"
    const val arch_unit = "com.tngtech.archunit:archunit-junit5:${LibVers.arch_unit}"
    const val faker = "com.github.javafaker:javafaker:${LibVers.faker}"
    const val testcontainers_postgresql = "org.testcontainers:postgresql:${LibVers.testcontainers}"
    const val testcontainers_rabbit = "org.testcontainers:rabbitmq:${LibVers.testcontainers}"
    const val testcontainers_core = "org.testcontainers:testcontainers:${LibVers.testcontainers}"
    const val wiremock = "com.github.tomakehurst:wiremock-jre8:${LibVers.wiremock}"
    const val kbdd = "ru.fix:kbdd:${LibVers.kbdd}"
    const val koin = "org.koin:koin-core:${LibVers.koin}"
    const val rest_assured = "io.rest-assured:rest-assured:${LibVers.rest_assured}"
    const val rest_assured_kotlin = "io.rest-assured:kotlin-extensions:${LibVers.rest_assured}"
    const val spring_rabbit_test = "org.springframework.amqp:spring-rabbit-test:${LibVers.spring_rabbit_test}"

    const val jfix_corounit_engine = "ru.fix:corounit-engine:${LibVers.corounit}"
    const val jfix_corounit_allure = "ru.fix:corounit-allure:${LibVers.corounit}"

    // Telnet deps
    const val testng = "org.testng:testng:${LibVers.testng}"
    const val commons_net = "commons-net:commons-net:${LibVers.commons_net}"
    const val pact_consumer = "au.com.dius.pact.consumer:junit5:${LibVers.pact}"
    const val pact_provider = "au.com.dius.pact.provider:junit5:${LibVers.pact}"

    // Database
    const val postgresql = "org.postgresql:postgresql:${LibVers.postgresql}"
    const val liquibase = ("org.liquibase:liquibase-core:${LibVers.liquibase}")

    // REST
    const val hal_explorer = "org.webjars:hal-explorer:${LibVers.hal_explorer}"

    // resilience4j
    const val resilience4j_circuitbreaker = "io.github.resilience4j:resilience4j-circuitbreaker:" +
            LibVers.resilience4j_circuitbreaker
    const val resilience4j_bulkhead = "io.github.resilience4j:resilience4j-bulkhead:" +
            LibVers.resilience4j_bulkhead

}

object PluginVers {
    const val kotlin = Global.kotlin_version
    const val spring_boot = LibVers.spring_boot
    const val detekt = "1.19.0"
    const val detekt_formatting = detekt
    const val spring_dependency_management = "1.0.11.RELEASE"
    const val spring_kotlin = Global.kotlin_version
    const val update_dependencies = "0.36.0"
    const val owasp_dependencies = "6.1.0"
    const val pitest = "1.7.0"
    const val allure = "2.9.6"
    const val allure_cli = "2.15.0"
    const val allure_java = "2.15.0"
    const val gatling = "3.7.2"
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
    const val pitest = "info.solidsoft.pitest"
    const val javaTestFixtures = "java-test-fixtures"
    const val allure = "io.qameta.allure"
    const val gatling = "io.gatling.gradle"
}