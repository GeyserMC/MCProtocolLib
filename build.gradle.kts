plugins {
    `java-library`
    `maven-publish`
    id("net.kyori.indra") version "3.1.3"
    id("net.kyori.indra.git") version "3.1.3"
    id("net.kyori.indra.publishing") version "3.1.3"
}

indra {
    github("GeyserMC", "MCProtocolLib")

    gpl3OnlyLicense()
    publishReleasesTo("opencollab-release-repo", "https://repo.opencollab.dev/maven-releases/")
    publishSnapshotsTo("opencollab-snapshot-repo", "https://repo.opencollab.dev/maven-snapshots/")

    configurePublications {
        pom {
            name.set("MCProtocolLib")
            url.set("https://github.com/GeyserMC/MCProtocolLib/")
            organization {
                name.set("GeyserMC")
                url.set("https://github.com/GeyserMC")
            }
            developers {
                developer {
                    id.set("steveice10")
                    name.set("Steveice10")
                    email.set("Steveice10@gmail.com")
                }
                developer {
                    id.set("GeyserMC")
                    name.set("GeyserMC")
                    url.set("https://geysermc.org/")
                }
            }
        }

        versionMapping {
            usage(Usage.JAVA_API) { fromResolutionOf(JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME) }
            usage(Usage.JAVA_RUNTIME) { fromResolutionResult() }
        }
    }

    javaVersions {
        target(17)
        strictVersions()
        testWith(17)
        minimumToolchain(17)
    }
}

val repoName = if (version.toString().endsWith("SNAPSHOT")) "maven-snapshots" else "maven-releases"
publishing {
    repositories {
        maven("https://repo.opencollab.dev/${repoName}/") {
            credentials.username = System.getenv("OPENCOLLAB_USERNAME")
            credentials.password = System.getenv("OPENCOLLAB_PASSWORD")
            name = "opencollab"
        }
    }
}

repositories {
    maven("https://repo.opencollab.dev/maven-releases/") {
        name = "opencollab-releases"
    }

    maven("https://repo.opencollab.dev/maven-snapshots/") {
        name = "opencollab-snapshots"
    }

    maven("https://jitpack.io") {
        name = "jitpack"
    }

    mavenCentral()
}

dependencies {
    // Minecraft related libraries
    api("com.github.steveice10:opennbt:1.6")
    api("com.github.GeyserMC:mcauthlib:6621fd081c")

    // Kyori adventure
    api("net.kyori:adventure-text-serializer-gson:4.14.0")
    api("net.kyori:adventure-text-serializer-gson-legacy-impl:4.14.0")

    // Math utilities
    api("org.cloudburstmc.math:api:2.0")
    api("org.cloudburstmc.math:immutable:2.0")

    // Stripped down fastutil
    api("com.nukkitx.fastutil:fastutil-object-int-maps:8.5.3")
    api("com.nukkitx.fastutil:fastutil-int-object-maps:8.5.3")

    // Netty
    api("io.netty:netty-all:4.1.99.Final")
    api("io.netty:netty-codec-haproxy:4.1.99.Final")
    api("io.netty.incubator:netty-incubator-transport-native-io_uring:0.0.23.Final")

    // Annotations
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.7.3")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    // Test dependencies
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}

group = "com.github.steveice10"
version = "1.20.2-1-SNAPSHOT"
description = "MCProtocolLib is a simple library for communicating with Minecraft clients and servers."

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}
