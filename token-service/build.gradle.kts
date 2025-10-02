apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
apply(plugin = "io.quarkus")

dependencies {
    implementation("io.quarkus:quarkus-resteasy-reactive-jackson")
    implementation("io.quarkus:quarkus-resteasy-reactive")

    implementation("org.apache.camel.quarkus:camel-quarkus-http:3.5.0")
    implementation("org.apache.camel.quarkus:camel-quarkus-kafka:3.5.0")
    implementation("org.apache.camel.quarkus:camel-quarkus-core:3.5.0")
    implementation("org.apache.camel.quarkus:camel-quarkus-direct:3.5.0")
    implementation("org.apache.camel.quarkus:camel-quarkus-bean:3.5.0")
    implementation("org.apache.camel.quarkus:camel-quarkus-rest:3.5.0")
    implementation("org.apache.camel.quarkus:camel-quarkus-jackson:3.5.0")
    // OIDC for token validation
    implementation("io.quarkus:quarkus-oidc")
    implementation("io.quarkus:quarkus-security")
}

allOpen {
    annotation("jakarta.ws.rs.Path")
    annotation("jakarta.enterprise.context.ApplicationScoped")
    annotation("io.quarkus.test.junit.QuarkusTest")
}