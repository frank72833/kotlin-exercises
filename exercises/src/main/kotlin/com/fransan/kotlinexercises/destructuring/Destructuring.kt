package com.fransan.kotlinexercises.destructuring

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.Currency
import java.util.UUID

@JvmInline
value class AccountId(val value: UUID) {
    companion object {
        fun fromString(id: String): AccountId = AccountId(UUID.fromString(id))
    }
}

data class Account(
    val id: AccountId,
    val ownerName: String,
    val balance: BigDecimal,
    val currency: Currency,
    val createdDateTime: LocalDateTime? = null,
)

fun main(args: Array<String>) {
    val account = Account(
        id = AccountId(UUID.randomUUID()),
        ownerName = "Owner name",
        balance = 1.03.toBigDecimal(),
        currency = Currency.getInstance("EUR"),
        createdDateTime = LocalDateTime.now(),
    )

    val (id, ownerName, balance, currency, createdDateTime) = account

    println("""
    id: $id
    ownerName: $ownerName
    balance: $balance
    currency: $currency
    createdDateTime: $createdDateTime
    """.trimIndent())
}