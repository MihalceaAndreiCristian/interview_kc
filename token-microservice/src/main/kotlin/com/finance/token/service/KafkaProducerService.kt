package com.finance.token.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.finance.token.model.UserTransactionsMessage
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.apache.camel.ProducerTemplate
import org.jboss.logging.Logger

@ApplicationScoped
class KafkaProducerService {

    @Inject
    lateinit var producerTemplate: ProducerTemplate

    @Inject
    lateinit var objectMapper: ObjectMapper

    private val log = Logger.getLogger(KafkaProducerService::class.java)

    fun publishUserTransactions(message: UserTransactionsMessage) {
        try {
            val jsonMessage = objectMapper.writeValueAsString(message)

            log.info("Publishing user transactions to Kafka for user: ${message.userId}, transaction count: ${message.transactionCount}")

            // Send to Kafka using Camel ProducerTemplate
            producerTemplate.sendBodyAndHeader(
                "kafka:user-transactions?brokers={{kafka.bootstrap.servers}}",
                jsonMessage,
                "kafka.KEY",
                message.userId
            )

            log.info("Successfully published user transactions to Kafka for user: ${message.userId}")
        } catch (e: Exception) {
            log.error("Failed to publish user transactions to Kafka", e)
            throw e
        }
    }
}