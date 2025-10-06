package com.finance.token.model

import jakarta.transaction.Transaction
import java.time.LocalDateTime

data class TokenResponse(
    val access_token: String,
    val expires_in: Long?,
    val refresh_token: String?,
    val token_type: String?,
    val scope: String?
)

// User transactions message for Kafka
data class UserTransactionsMessage(
    val userId: String,
    val username: String,
    val transactionCount: Int,
    val transactions: List<Transaction>,
    val retrievedAt: LocalDateTime
)