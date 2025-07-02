import java.net.URI

plugins {
    id("mcprotocollib.base-conventions")
    id("net.kyori.indra.publishing")
}

indra {
//    publishSnapshotsTo("geysermc", "https://repo.opencollab.dev/maven-snapshots")
//    publishReleasesTo("geysermc", "https://repo.opencollab.dev/maven-releases")

    configurePublications {
        pom {
            organization {
                name = "GeyserMC"
                url = "https://github.com/GeyserMC"
            }
        }
    }
}

publishing {
    repositories {
        maven {
            name = "geysermc"
            url = URI.create(
                when {
                    project.version.toString().endsWith("-SNAPSHOT") ->
                        "https://repo.opencollab.dev/maven-snapshots"
                    else ->
                        "https://repo.opencollab.dev/maven-releases"
                }
            )
            credentials(PasswordCredentials::class.java)
        }
    }
}
