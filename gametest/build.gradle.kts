plugins {
    java
    alias(libs.plugins.loom)
}

// Not needed for gametest
tasks.named("javadoc") {
    enabled = false
}

// Loom adds its own project level repositories, which makes Gradle ignore the ones declared in
// settings.gradle.kts for this project
repositories {
    maven("https://repo.opencollab.dev/main") {
        name = "opencollab-main"
    }
    mavenCentral()
}

dependencies {
    minecraft(libs.minecraft)
    implementation(libs.fabric.loader)
    implementation(libs.fabric.api)

    // Minecraft already provides netty, fastutil, gson and slf4j
    implementation(projects.protocol) {
        exclude(group = "io.netty")
        exclude(group = "com.nukkitx.fastutil")
        exclude(group = "org.slf4j")
    }
}

loom {
    mods {
        register("mcpl-gametest") {
            sourceSet(sourceSets.main.get())
        }
    }
}

fabricApi {
    configureTests {
        modId = "mcpl-gametest"
        enableClientGameTests = false
        eula = true
    }
}

// Minecraft 26.2 requires Java 25
java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}
