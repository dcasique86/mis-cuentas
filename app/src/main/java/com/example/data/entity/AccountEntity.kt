package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class AccountType(val displayName: String) {
    CASH("Efectivo"),
    WALLET("Billetera digital"),
    BANK("Cuenta bancaria"),
    CREDIT_CARD("Tarjeta de crédito"),
    SAVINGS("Ahorro")
}

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val type: String, // CASH, WALLET, BANK, CREDIT_CARD, SAVINGS
    val currentBalance: Double,
    val creditLimit: Double? = null, // Solo para tarjetas de crédito
    val iconName: String = "bank", // cash, nequi, bancolombia, davivienda, credit_card, savings
    val colorHex: Long = 0xFF4A685D,
    val isDefault: Boolean = false,
    val note: String = ""
)
