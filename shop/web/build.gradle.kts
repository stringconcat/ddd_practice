dependencies {
    // project
    implementation(project(":common:types"))
    implementation(project(":shop:domain"))
    implementation(project(":shop:usecase"))
    implementation(project(":shop:query"))

    // kotlin
    implementation(Libs.kotlin_jdk8)
    implementation(Libs.kotlin_reflect)
    implementation(Libs.kotlin_stdlib)
    implementation(Libs.arrow)

    //spring
    implementation(Libs.spring_boot_starter_web)
    implementation(Libs.spring_boot_starter_thymeleaf)
}