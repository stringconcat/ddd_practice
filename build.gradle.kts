import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

val parentProjectDir = projectDir

plugins {
    id(Plugins.kotlin) version PluginVers.kotlin apply false
    id(Plugins.detekt) version PluginVers.detekt
    id(Plugins.update_dependencies) version PluginVers.update_dependencies
    id(Plugins.owasp_dependencies) version PluginVers.owasp_dependencies
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

    configurations.all {
        resolutionStrategy {
            eachDependency {
                requested.version?.contains("snapshot", true)?.let {
                    if(it){
                        throw GradleException("Snapshot found: ${requested.name} ${requested.version}")
                    }
                }
            }
        }
    }


    apply {
        plugin("java")
        plugin(Plugins.detekt)
        plugin("jacoco")
        plugin(Plugins.update_dependencies)
        plugin(Plugins.owasp_dependencies)
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
        val dependencyUpdate =
            named<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask>("dependencyUpdates")


        dependencyUpdate {
            revision = "release"
            outputFormatter = "txt"
            checkForGradleUpdate = true
            outputDir = "${buildDir}/reports/dependencies"
            reportfileName = "updates"
        }


        check {
            finalizedBy(jacocoTestReport)
            finalizedBy(dependencyUpdate)
        }

        jacocoTestReport {
            dependsOn(check)
            finalizedBy(jacocoTestCoverageVerification)
        }

        jacocoTestCoverageVerification {
            dependsOn(jacocoTestReport)

            violationRules {

                rule {
                    excludes = listOf("web")

                    limit {
                        minimum = BigDecimal("0.9")
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