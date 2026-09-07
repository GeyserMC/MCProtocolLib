plugins {
    id("mcprotocollib.publish-conventions")
}

version = "0.1.0"
description = "Adventure text component serializers for Cloudburst's NBT library"

dependencies {
    api(libs.jspecify)
    api(libs.adventure.api)
    api(libs.cloudburstnbt)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}
