plugins {
    kotlin("jvm")
    id("io.quarkus")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(enforcedPlatform("io.quarkus:quarkus-bom:3.5.0"))
    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-jsonp")

    // Camel Core + Quarkus bindings
    implementation(enforcedPlatform("org.apache.camel.quarkus:camel-quarkus-bom:3.5.0"))
    implementation("org.apache.camel.quarkus:camel-quarkus-core:3.5.0")
    implementation("org.apache.camel.quarkus:camel-quarkus-platform-http:3.5.0")
    implementation("org.apache.camel.quarkus:camel-quarkus-rest:3.5.0")
    implementation("org.apache.camel.quarkus:camel-quarkus-http:3.5.0")
    implementation("org.apache.camel.quarkus:camel-quarkus-direct:3.5.0")
    implementation("org.apache.camel.quarkus:camel-quarkus-jsonpath:3.5.0")
    implementation("org.apache.camel.quarkus:camel-quarkus-bean:3.5.0")
    implementation("org.apache.camel.quarkus:camel-quarkus-jackson:3.5.0")
    implementation("org.apache.camel.quarkus:camel-quarkus-xml-jaxb:3.5.0")
    implementation("org.apache.camel.quarkus:camel-quarkus-xml-io-dsl:3.5.0")
    // OAuth / Keycloak (using HTTP + Jackson)
    // If you prefer camel-vertx-http or camel-jetty, swap accordingly.

    // Kafka
    implementation("org.apache.camel.quarkus:camel-quarkus-kafka:3.5.0")

    implementation("com.nimbusds:nimbus-jose-jwt:9.37.3")


    // Config
    implementation("io.quarkus:quarkus-config-yaml")

    // Logging
    implementation("io.quarkus:quarkus-logging-json")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.test {
    useJUnitPlatform()
}
