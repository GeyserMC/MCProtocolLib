# MCProtocolLib

MCProtocolLib is a simple library for communicating with Minecraft clients and servers. It allows developers to build custom bots, clients, or servers for Minecraft with ease.

## Example Code

See [example/com/github/steveice10/mc/protocol/test/MinecraftProtocolTest.java](https://github.com/GeyserMC/MCProtocolLib/tree/master/example/com/github/steveice10/mc/protocol/test) for sample usage.

## Adding as a Dependency

The recommended way of installing MCProtocolLib is through [JitPack](https://jitpack.io/). Visit [MCProtocolLib on JitPack](https://jitpack.io/#GeyserMC/MCProtocolLib) for more details on how to include MCProtocolLib in your
project.

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.GeyserMC</groupId>
    <artifactId>MCProtocolLib</artifactId>
    <version>(version here)</version>
</dependency>
```

### Gradle

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.GeyserMC:MCProtocolLib:(version here)'
}
```

## Building the Source

MCProtocolLib uses Maven to manage dependencies. To build the source code, run `mvn clean install` in the project root directory.

## Support and Development

Please join [the GeyserMC Discord server](https://discord.gg/geysermc) and visit the **#mcprotocollib** channel for discussion and support for this project.

## License

MCProtocolLib is licensed under the **[MIT license](http://www.opensource.org/licenses/mit-license.html)**.
