plugins {
    id(Plugins.allure) version PluginVers.allure
}

allure {
//
//    reporting {
//        baseDir = File("$buildDir/reports")
//    }
//
//    report {
//        // There might be several tasks producing the report, so the property
//        // configures a base directory for all the reports
//        // Each task creates its own subfolder there
//        reportDir.set(project.reporting.baseDirectory.dir("allure-report"))
//    }

    adapter {
        version.set(PluginVers.allure_cli)
        allureJavaVersion.set(PluginVers.allure_java)

        frameworks {
            junit5 {
                enabled.set(false)
            }
        }
    }
}

dependencies {

    // kotlin
    implementation(Libs.kotlin_jdk8)
    implementation(Libs.kotlin_reflect)
    implementation(Libs.kotlin_stdlib)
    implementation(Libs.arrow)

    // test
    testImplementation(Libs.kotest_junit)
    testImplementation(Libs.kotest_arrow)
    testImplementation(Libs.junit_engine)
    testImplementation(Libs.junit_params)

    testImplementation(Libs.kbdd)
    testImplementation(Libs.rest_assured)
    testImplementation(Libs.jackson_kotlin)
    testImplementation(Libs.jackson_databind)
    testImplementation(Libs.jfix_corounit_allure)
    testImplementation(Libs.jfix_corounit_engine)
    testImplementation(Libs.koin)

    testFixturesImplementation(Libs.arrow)
    testFixturesImplementation(Libs.faker)
}