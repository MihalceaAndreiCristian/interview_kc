apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
apply(plugin = "io.quarkus")

dependencies {
    implementation("io.quarkus:quarkus-resteasy-reactive-jackson")
    implementation("io.quarkus:quarkus-resteasy-reactive")

//    implementation("org.apache.camel.quarkus:camel-quarkus-http:3.5.0")
//    implementation("org.apache.camel.quarkus:camel-quarkus-kafka:3.5.0")
//    implementation("org.apache.camel.quarkus:camel-quarkus-core:3.5.0")
//    implementation("org.apache.camel.quarkus:camel-quarkus-direct:3.5.0")
//    implementation("org.apache.camel.quarkus:camel-quarkus-bean:3.5.0")
//    implementation("org.apache.camel.quarkus:camel-quarkus-rest:3.5.0")
//    implementation("org.apache.camel.quarkus:camel-quarkus-jackson:3.5.0")
    implementation("org.apache.camel.quarkus:camel-quarkus-core")
    implementation("org.apache.camel.quarkus:camel-quarkus-platform-http")
    implementation("org.apache.camel.quarkus:camel-quarkus-http")
    implementation("org.apache.camel.quarkus:camel-quarkus-direct:3.5.0")

    implementation("io.quarkus:quarkus-oidc-client")
    implementation("com.auth0:java-jwt:4.4.0") // pentru semnare JWT

    // OIDC for token validation
    implementation("io.quarkus:quarkus-oidc")
    implementation("io.quarkus:quarkus-security")
}

allOpen {
    annotation("jakarta.ws.rs.Path")
    annotation("jakarta.enterprise.context.ApplicationScoped")
    annotation("io.quarkus.test.junit.QuarkusTest")
}