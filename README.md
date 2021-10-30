# MCProtocolLib
MCProtocolLib is a simple library for communicating with a Minecraft client/server. It aims to allow people to make custom bots, clients, or servers for Minecraft easily.

## Example Code
See [example/com/github/steveice10/mc/protocol/test/MinecraftProtocolTest.java](https://github.com/GeyserMC/MCProtocolLib/tree/master/example/com/github/steveice10/mc/protocol/test)

## Adding as a Dependency

The recommended way of fetching MCProtocolLib is through jitpack.io. See [here](https://jitpack.io/#GeyserMC/MCProtocolLib) for more details on how to include MCProtocolLib in your project.

Maven:
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

Gradle:
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
MCProtocolLib uses Maven to manage dependencies. Simply run 'mvn clean install' in the source's directory.

## Support and development

Please join us at https://discord.gg/geysermc under #mcprotocollib for discussion and support for this project.

## License
MCProtocolLib is licensed under the **[MIT license](http://www.opensource.org/licenses/mit-license.html)**.

