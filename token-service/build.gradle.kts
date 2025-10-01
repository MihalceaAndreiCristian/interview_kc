plugins {
    kotlin("jvm")
    id("io.quarkus")  // Add the Quarkus plugin
}

group = "ro.amihalcea"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("io.quarkus.platform:quarkus-bom:3.5.0"))
    implementation("org.apache.camel.quarkus:camel-quarkus-core:3.5.0")
    implementation("org.apache.camel.quarkus:camel-quarkus-direct:3.5.0")
    implementation("org.apache.camel.quarkus:camel-quarkus-rest:3.5.0")
    implementation("org.apache.camel.quarkus:camel-quarkus-jackson:3.5.0")

    implementation("org.apache.camel.quarkus:camel-quarkus-kafka:3.22.0")
    // Quarkus core
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-agroal")
    implementation("io.quarkus:quarkus-container-image-jib")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}