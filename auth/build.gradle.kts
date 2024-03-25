plugins {
    id("mcprotocollib.publish-conventions")
}

version = "2.0.0-SNAPSHOT"
description = "A library for authentication with Minecraft accounts."

dependencies {
    // Gson
    api(libs.gson)

    // Checker Framework
    api(libs.checkerframework.qual)

    // Test dependencies
    testImplementation(libs.junit.jupiter)
}
