buildscript {
    repositories {
        jcenter()
        mavenCentral()
        mavenLocal()
    }
    dependencies {
        classpath(Libs.kotlin_stdlib)
        classpath(Libs.kotlin_jdk8)
        classpath(Libs.kotlin_reflect)
    }
}

repositories {
    jcenter()
    mavenCentral()
    mavenLocal()
}

plugins {
    java
    id(Plugins.kotlin)
    jacoco
}

dependencies {
    // project
    implementation(project(":common:types"))

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
}

tasks.withType<Test> {
    useJUnitPlatform()
}
