plugins {
    id("mcprotocollib.publish-conventions")
}

dependencies {
    api(libs.jspecify)
    api(libs.adventure.api)
    api(libs.cloudburstnbt)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}
