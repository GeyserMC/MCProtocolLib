plugins {
    java
}

dependencies {
    implementation(projects.protocol)
}

tasks.javadoc {
    enabled = false
}
