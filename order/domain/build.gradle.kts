
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
}


dependencies {

    // kotlin
    implementation(Libs.kotlin_jdk8)
    implementation(Libs.kotlin_reflect)
    implementation(Libs.kotlin_stdlib)


}

tasks.withType<Test> {
    useJUnitPlatform()
}


