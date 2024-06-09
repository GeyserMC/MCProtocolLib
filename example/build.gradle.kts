plugins {
    java
}

dependencies {
    implementation(projects.protocol)
    implementation(libs.slf4j.simple)
}

tasks.javadoc {
    enabled = false
}
