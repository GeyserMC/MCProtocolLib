dependencyResolutionManagement {
    repositories {
        maven("https://repo.opencollab.dev/maven-releases/") {
            name = "opencollab-releases"
        }
        maven("https://repo.opencollab.dev/maven-snapshots/") {
            name = "opencollab-snapshots"
        }
        // fixme: remove when adventure 4.15.0 releases
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
            name = "sonatype-snapshots"
        }
        maven("https://jitpack.io") {
            name = "jitpack"
        }
        mavenCentral()
    }
}

rootProject.name = "mcprotocollib"

include("example")
