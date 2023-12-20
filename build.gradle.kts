plugins {
    idea
    `java-library`
    `maven-publish`
    alias(libs.plugins.indra)
    alias(libs.plugins.indra.git)
    alias(libs.plugins.indra.publishing)
    alias(libs.plugins.lombok)
}

indra {
    github("GeyserMC", "MCProtocolLib")

    mitLicense()
    publishReleasesTo("opencollab-release-repo", "https://repo.opencollab.dev/maven-releases/")
    publishSnapshotsTo("opencollab-snapshot-repo", "https://repo.opencollab.dev/maven-snapshots/")

    configurePublications {
        pom {
            name = "MCProtocolLib"
            url = "https://github.com/GeyserMC/MCProtocolLib/"
            organization {
                name = "GeyserMC"
                url = "https://github.com/GeyserMC"
            }
            developers {
                developer {
                    id = "steveice10"
                    name = "Steveice10"
                    email = "Steveice10@gmail.com"
                }
                developer {
                    id = "GeyserMC"
                    name = "GeyserMC"
                    url = "https://geysermc.org/"
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

dependencies {
    // Minecraft related libraries
    api(libs.opennbt)
    api(libs.mcauthlib)

    // Kyori adventure
    api(libs.bundles.adventure)

    // Math utilities
    api(libs.bundles.math)

    // Stripped down fastutil
    api(libs.bundles.fastutil)

    // Netty
    api(libs.bundles.netty)

    // Test dependencies
    testImplementation(libs.junit.jupiter)
}

lombok {
    version = libs.versions.lombok.version.get()
}

group = "com.github.steveice10"
version = "1.20.4-2-SNAPSHOT"
description = "MCProtocolLib is a simple library for communicating with Minecraft clients and servers."

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-Xlint:all,-processing")
}

tasks.withType<Javadoc> {
    title = "MCProtocolLib Javadocs"
    val options = options as StandardJavadocDocletOptions
    options.encoding = "UTF-8"
    options.addStringOption("Xdoclint:all,-missing", "-quiet")
}

