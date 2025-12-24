import java.net.URI

plugins {
    id("mcprotocollib.publish-conventions")
    jacoco
}

version = "1.21.11-feature-custom-entities"
description = "MCProtocolLib is a simple library for communicating with Minecraft clients and servers."

dependencies {
    // Minecraft related libraries
    api(libs.cloudburstnbt)

    // Gson
    api(libs.gson)

    // MinecraftAuth for authentication
    api(libs.minecraftauth)

    // Slf4j
    api(libs.slf4j.api)

    // Kyori adventure
    api(libs.bundles.adventure)

    // Math utilities
    api(libs.bundles.math)

    // Stripped down fastutil
    api(libs.bundles.fastutil)

    // Netty
    api(libs.netty.all)

    // Checker Framework
    api(libs.checkerframework.qual)

    // Test dependencies
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.slf4j.simple)
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = false
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
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
