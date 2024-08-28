# MCProtocolLib

MCProtocolLib is a simple library for communicating with Minecraft clients and servers. It allows developers to build custom bots, clients, or servers for Minecraft with ease.

## Example Code

See the [example](https://github.com/GeyserMC/MCProtocolLib/tree/master/example/src/main/java/org/geysermc/mcprotocollib) folder for sample usage.

## Adding as a Dependency

MCProtocolLib builds are published to the [Open Collaboration repository](https://repo.opencollab.dev/#/maven-snapshots/org/geysermc/mcprotocollib/protocol).
Follow the below steps to add MCProtocolLib as a dependency to your project.

### Maven

#### Add the Repository

```xml
<repositories>
    <repository>
        <id>opencollab</id>
        <url>https://repo.opencollab.dev/maven-snapshots/</url>
    </repository>
</repositories>
```

#### Add the Dependency

```xml
<dependency>
    <groupId>org.geysermc.mcprotocollib</groupId>
    <artifactId>protocol</artifactId>
    <version>(version here)</version>
</dependency>
```

### Gradle (Groovy DSL)

#### Add the Repository

```groovy
repositories {
    maven { 
        name 'opencollab'
        url 'https://repo.opencollab.dev/maven-snapshots/'
    }
}
```

#### Add the Dependency

```groovy
dependencies {
    implementation 'org.geysermc.mcprotocollib:protocol:(version here)'
}
```

### Gradle (Kotlin DSL)

#### Add the Repository

```kotlin
repositories {
    maven("https://repo.opencollab.dev/maven-snapshots/") {
        name = "opencollab"
    }
}
```

#### Add the Dependency

```kotlin
dependencies {
    implementation("org.geysermc.mcprotocollib:protocol:(version here)")
}
```

### Snapshots

To use snapshot builds, switch the URL to `https://repo.opencollab.dev/maven-snapshots/`.

### Javadocs

You can find the Javadocs for MCProtocolLib [on opencollab](https://ci.opencollab.dev/job/GeyserMC/job/MCProtocolLib/job/master/javadoc/overview-summary.html).

## Building the Source

MCProtocolLib uses Gradle to manage dependencies. To build the source code, run `./gradlew clean build` in the project root directory.

## Support and Development

Please join [the GeyserMC Discord server](https://discord.gg/geysermc) and visit the **#mcprotocollib** channel for discussion and support for this project.

## License

MCProtocolLib is licensed under the **[MIT license](https://opensource.org/license/mit)**.
