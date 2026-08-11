import java.net.URI

plugins {
    id("mcprotocollib.publish-conventions")
}

version = "26.2-SNAPSHOT"
description = "A serializer between adventure text components and their Minecraft: Java Edition nbt representation."

dependencies {
    // The component model this serializer produces and consumes
    api(libs.adventure.api)

    // The nbt model this serializer produces and consumes
    api(libs.cloudburstnbt)

    // Test dependencies
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}

publishing {
    repositories {
        maven {
            name = "geysermc"
            url = URI.create(
                when {
                    version.toString().endsWith("-SNAPSHOT") ->
                        "https://repo.opencollab.dev/maven-snapshots"
                    else ->
                        "https://repo.opencollab.dev/maven-releases"
                }
            )
            credentials(PasswordCredentials::class.java)
        }
    }
}
