dependencyResolutionManagement {
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
}

rootProject.name = "mcprotocollib"

include("example")