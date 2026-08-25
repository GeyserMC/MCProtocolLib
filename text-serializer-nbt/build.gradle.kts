plugins {
    id("mcprotocollib.publish-conventions")
}

dependencies {
    api(libs.jspecify)
    api(libs.adventure.api)
    api(libs.cloudburstnbt)

    testImplementation(libs.junit.jupiter)
}
