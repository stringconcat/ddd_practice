dependencies {

    // logging
    implementation(Libs.slf4j_api)

    // test
    testImplementation(Libs.testng)
    testImplementation(Libs.commons_net)
}

tasks{
    withType<Test> {
        useTestNG()
    }
}