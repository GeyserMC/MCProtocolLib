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

lombok {
    // ugh https://discuss.gradle.org/t/precompiled-script-plugin-accessing-another-precompiled-script-plugin-extension/46177/4
    version = project.rootProject
        .extensions
        .getByType(VersionCatalogsExtension::class.java)
        .named("libs")
        .findVersion("lombok")
        .get()
        .displayName
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