# MCProtocolLib

MCProtocolLib is a simple library for communicating with Minecraft clients and servers. It allows developers to build custom bots, clients, or servers for Minecraft with ease.

## Example Code

See [example/com/github/steveice10/mc/protocol/test/MinecraftProtocolTest.java](https://github.com/GeyserMC/MCProtocolLib/tree/master/example/com/github/steveice10/mc/protocol/test) for sample usage.

## Adding as a Dependency

MCProtocolLib builds are published to the [Open Collaboration repository](https://repo.opencollab.dev/ui/packages/gav:%2F%2Fcom.github.steveice10:mcprotocollib). Follow the below steps to add MCProtocolLib as a dependency to your project.

### Maven

```xml
<repositories>
    <repository>
        <id>opencollab</id>
        <url>https://repo.opencollab.dev/maven-releases/</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.steveice10</groupId>
    <artifactId>mcprotocollib</artifactId>
    <version>(version here)</version>
</dependency>
```

### Gradle

```groovy
allprojects {
    repositories {
        maven { url 'https://repo.opencollab.dev/maven-releases/' }
    }
}

dependencies {
    implementation 'com.github.steveice10:mcprotocollib:(version here)'
}
```

### Snapshots

To use snapshot builds, switch the URL to `https://repo.opencollab.dev/maven-snapshots/`.

## Building the Source

MCProtocolLib uses Maven to manage dependencies. To build the source code, run `mvn clean install` in the project root directory.

## Support and Development

Please join [the GeyserMC Discord server](https://discord.gg/geysermc) and visit the **#mcprotocollib** channel for discussion and support for this project.

## License

MCProtocolLib is licensed under the **[MIT license](http://www.opensource.org/licenses/mit-license.html)**.
