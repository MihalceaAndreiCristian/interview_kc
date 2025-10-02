package com.finance.token.route

import jakarta.enterprise.context.ApplicationScoped
import org.apache.camel.CamelContext
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.rest.RestParamType

@ApplicationScoped
class TokenRoute(context: CamelContext) : RouteBuilder(context) {
    override fun configure() {
        // REST endpoint
        rest("/token")
            .get()
            .param().name("code").required(true).type(RestParamType.query).endParam()
            .to("direct:exchangeCode")

        // Exchange code -> JWT
        from("direct:exchangeCode")
            .log("Received auth code: \${header.code}")
            .setHeader("CamelHttpMethod").constant("POST")
            .setHeader("Content-Type").constant("application/x-www-form-urlencoded")
            .setBody().simple("grant_type=authorization_code&code=\${header" +
                    ".code}&client_id=finance-client&redirect_uri=http://localhost:8081/token")
            .to("rest:post://realms/finance-app/protocol/openid-connect/token?host" +
                    "=localhost:8080&bridgeEndpoint=true" +
                    "&throwExceptionOnFailure=true")
            .unmarshal().json()
            .log("Body: \${body}")
            .setHeader("Authorization").simple("Bearer \${body[access_token]}")
            .to("direct:fetchTransactions")
//            .log("Received auth code: \${header.code}")
//            .toD("http://localhost:8080/realms/finance-app/protocol/openid-connect/token"
//                    + "?httpMethod=POST"
//                    + "&bridgeEndpoint=true"
//                    + "&throwExceptionOnFailure=true")
//            .unmarshal().json()
//            .setHeader("Authorization").simple("Bearer \${body[access_token]}")
//            .to("direct:fetchTransactions")

        // Call transactions-service
        from("direct:fetchTransactions")
            .to("http://localhost:8082/api/transactions/bridgeEndpoint=true")
            .unmarshal().json()
            .log("Sending to Kafka at localhost:9092\nBody: \${body}")
//            .to("kafka:user-transactions?brokers=localhost:9092")
            .log("Published transactions to Kafka")
    }
}