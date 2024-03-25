plugins {
    java
}

dependencies {
    implementation(projects.protocol)
    implementation(projects.auth)
}

tasks.javadoc {
    enabled = false
}
