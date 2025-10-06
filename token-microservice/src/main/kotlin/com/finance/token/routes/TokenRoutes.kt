package com.finance.token.routes

import jakarta.enterprise.context.ApplicationScoped
import org.apache.camel.builder.RouteBuilder

@ApplicationScoped
class TokenRoutes : RouteBuilder() {
    override fun configure() {

        // Error handling
        onException(Exception::class.java)
            .handled(true)
            .log("Error in token pipeline: \${exception.message}")
            .setHeader("Content-Type").constant("application/json")
            .setBody().simple("{\"error\":\"\${exception.message}\"}")

        // 1) Receive GET /token?code=...
        from("platform-http:/token?httpMethodRestrict=GET")
            .routeId("token-receive")
            .log("Received auth code: \${header.code}")
            .validate().simple("\${header.code} != null")
            .to("direct:exchange-token")

        // 2) Exchange authorization code for access token at Keycloak
        from("direct:exchange-token")
            .routeId("keycloak-exchange")
            .setHeader("Content-Type").constant("application/x-www-form-urlencoded")
            .process { ex ->
                val code = ex.`in`.getHeader("code", String::class.java)
                val redirectUri = ex.context.resolvePropertyPlaceholders("{{keycloak.redirect-uri}}")
                val clientId = ex.context.resolvePropertyPlaceholders("{{keycloak.client-id}}")
                val tokenEndpoint = ex.context.resolvePropertyPlaceholders("{{keycloak.token-endpoint}}")
                val privateKeyPath = ex.context.resolvePropertyPlaceholders("{{keys.private}}")

                val clientAssertion = com.finance.token.service.JwtSigner.signAssertion(clientId, tokenEndpoint,
                    privateKeyPath)

                val body = "grant_type=authorization_code" +
                        "&code=$code" +
                        "&redirect_uri=$redirectUri" +
                        "&client_id=$clientId" +
                        "&client_assertion_type=urn:ietf:params:oauth:client-assertion-type:jwt-bearer" +
                        "&client_assertion=$clientAssertion"

                ex.`in`.body = body
            }
            .log("\nBody signed \${body}\n")
//            .toD("http:{{keycloak.token-endpoint}}?httpMethod=POST&bridgeEndpoint=true")
            .toD("http://localhost:8080/realms/finance-app/protocol/openid-connect/token"
                    + "?httpMethod=POST"
                    + "&bridgeEndpoint=true"
                    + "&throwExceptionOnFailure=true")
            .unmarshal().json()
            .log("Token exchange response: \${body}")
            .to("direct:parse-token")

        // 3) Parse token JSON into a POJO and call transactions-service
        from("direct:parse-token")
            .routeId("parse-token")
            .process { ex ->
                val map = ex.`in`.body as Map<*, *>
                val token = map["access_token"] as? String
                require(!token.isNullOrBlank()) { "access_token missing in token response" }
                ex.setProperty("access_token", token)
            }
            .setHeader("Authorization").simple("Bearer \${exchangeProperty.access_token}")
            .toD("http://localhost:8082/api/transactions?httpMethod=GET&bridgeEndpoint=true")
            .unmarshal().json()
            .log("Transactions fetched: \${body}")
            .to("direct:publish-kafka")

        // 4) Publish to Kafka
        from("direct:publish-kafka")
            .routeId("kafka-publish")
            .process { ex ->
                val transactions = ex.`in`.body
                val user = "current-user" // optionally decode token sub claim
                val payload = mapOf("user" to user, "transactions" to transactions)
                ex.`in`.body = payload
            }
            .marshal().json()
            .toD("kafka:user-transactions?brokers=localhost:9092")
            .log("Published transactions to Kafka topic \${body}")
            .setHeader("Content-Type").constant("application/json")
            .setBody().simple("""{"status":"ok"}""")
    }
}
