plugins {
    java
}

dependencies {
    implementation(projects.protocol)
}

tasks.javadoc {
    onlyIf { false }
}
