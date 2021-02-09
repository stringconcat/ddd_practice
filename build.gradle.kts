import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

val parentProjectDir = projectDir

plugins {
    id(Plugins.kotlin) version PluginVers.kotlin apply false
    id(Plugins.detekt) version PluginVers.detekt
    id("com.github.ben-manes.versions") version "0.36.0"
}

/**
 * Project configuration by properties and environment
 */
fun envConfig() = object : ReadOnlyProperty<Any?, String?> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): String? =
        if (ext.has(property.name)) {
            ext[property.name] as? String
        } else {
            System.getenv(property.name)
        }
}



subprojects {
    group = "com.stringconcat.app"

    apply {
        plugin("java")
        plugin(Plugins.detekt)
        plugin("jacoco")
        plugin("com.github.ben-manes.versions")

    }

    repositories {
        jcenter()
        mavenCentral()
        mavenLocal()
    }

    detekt {
        config = files("$parentProjectDir/detekt/detekt-config.yml")
        buildUponDefaultConfig = true

        reports {
            html.enabled = true
        }

        dependencies {
            detektPlugins("${Plugins.detekt_formatting}:${PluginVers.detekt_formatting}")
        }
    }



    tasks {



        val check = named<DefaultTask>("check")

        val jacocoTestReport = named<JacocoReport>("jacocoTestReport")
        val jacocoTestCoverageVerification = named<JacocoCoverageVerification>("jacocoTestCoverageVerification")

        check {
            finalizedBy(jacocoTestReport)
        }

        jacocoTestReport {
            dependsOn(check)
            finalizedBy(jacocoTestCoverageVerification)
        }

        jacocoTestCoverageVerification {
            dependsOn(jacocoTestReport)

            violationRules {
                rule {

                    element = "CLASS"
                    excludes = listOf("com.stringconcat.dev.course.app.MainKt")
                    limit {
                        minimum = BigDecimal(1)
                    }
                }
            }

        }



        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
                jvmTarget = JavaVersion.VERSION_11.toString()
                allWarningsAsErrors = true
                freeCompilerArgs = listOf("-Xjvm-default=enable")
            }
        }


        withType<JavaCompile> {
            options.compilerArgs.add("-Xlint:all")
        }


        withType<Test> {
            useJUnitPlatform()

            maxParallelForks = 10

            testLogging {
                events(
                    org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
                )
                showStandardStreams = true
                exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            }
        }


    }
}