plugins {
    java
}

dependencies {
    implementation(rootProject)
}

tasks.javadoc {
    onlyIf { false }
}
