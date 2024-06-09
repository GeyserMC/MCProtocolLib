plugins {
    `java-library`
    id("net.kyori.indra")
    id("net.kyori.indra.git")
    id("io.freefair.lombok")
}

indra {
    github("GeyserMC", "MCProtocolLib") {
        ci(true)
        issues(true)
        scm(true)
    }
    mitLicense()

    javaVersions {
        target(17)
    }
}

if (System.getenv("JITPACK") == "true") {
    tasks.withType<Sign>().configureEach {
        onlyIf { false }
    }
}
lombok {
    version = libs.versions.lombok.version.get()
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-Xlint:all,-processing")
}

tasks.withType<Javadoc> {
    title = "MCProtocolLib Javadocs"
    val options = options as StandardJavadocDocletOptions
    options.encoding = "UTF-8"
    options.addStringOption("Xdoclint:all,-missing", "-quiet")
}
