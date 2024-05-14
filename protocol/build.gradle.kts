plugins {
    id("mcprotocollib.publish-conventions")
}

version = "1.20.6-1"
description = "MCProtocolLib is a simple library for communicating with Minecraft clients and servers."

dependencies {
    // Minecraft related libraries
    api(libs.cloudburstnbt)
    api(libs.mcauthlib)

    // Kyori adventure
    api(libs.bundles.adventure)

    // Math utilities
    api(libs.bundles.math)

    // Stripped down fastutil
    api(libs.bundles.fastutil)

    // Netty
    api(libs.bundles.netty)

    // Checker Framework
    api(libs.checkerframework.qual)

    // Test dependencies
    testImplementation(libs.junit.jupiter)
}
