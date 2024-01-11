plugins {
    id("mcprotocollib.base-conventions")
    id("net.kyori.indra.publishing")
}

indra {
    publishSnapshotsTo("geysermc", "https://repo.opencollab.dev/maven-snapshots")
    publishReleasesTo("geysermc", "https://repo.opencollab.dev/maven-releases")

    configurePublications {
        pom {
            organization {
                name = "GeyserMC"
                url = "https://github.com/GeyserMC"
            }
        }
    }
}
