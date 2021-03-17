val parentProjectDir = projectDir

plugins {
    id(Plugins.kotlin) version PluginVers.kotlin apply false
    id(Plugins.detekt) version PluginVers.detekt
    id(Plugins.update_dependencies) version PluginVers.update_dependencies
    id(Plugins.owasp_dependencies) version PluginVers.owasp_dependencies
}

subprojects {

    configurations.all {
        resolutionStrategy {
            eachDependency {
                requested.version?.contains("snapshot", true)?.let {
                    if (it) {
                        throw GradleException("Snapshot found: ${requested.name} ${requested.version}")
                    }
                }
            }
        }
    }

    apply {
        plugin("java")
        plugin(Plugins.kotlin)
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
        input = files("src/main/kotlin", "src/test/kotlin")

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
            outputDir = "$buildDir/reports/dependencies"
            reportfileName = "updates"
        }

        dependencyUpdate.configure {

            fun isNonStable(version: String): Boolean {
                val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
                val regex = "^[0-9,.v-]+(-r)?$".toRegex()
                val isStable = stableKeyword || regex.matches(version)
                return isStable.not()
            }

            rejectVersionIf {
                isNonStable(candidate.version) && !isNonStable(currentVersion)
            }
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
                    excludes = listOf("application", "telnet")
                    limit {
                        minimum = BigDecimal("0.93")
                    }
                }
            }
        }

        val failOnWarning = project.properties["allWarningsAsErrors"] != null && project
            .properties["allWarningsAsErrors"] == "true"

        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
                jvmTarget = JavaVersion.VERSION_11.toString()
                allWarningsAsErrors = failOnWarning
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