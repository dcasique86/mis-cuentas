package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "debts")
data class DebtEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String, // Carlos, Daniel, Tarjeta de crédito, etc.
    val totalAmount: Double,
    val paidAmount: Double = 0.0,
    val isIOwe: Boolean, // true = "Por pagar" (yo debo), false = "Por cobrar" (me deben)
    val dueDate: Long? = null,
    val note: String = "",
    val category: String = "General",
    val createdAt: Long = System.currentTimeMillis(),
    val lastPaymentDate: Long? = null
) {
    val remainingAmount: Double
        get() = (totalAmount - paidAmount).coerceAtLeast(0.0)

    val isSettled: Boolean
        get() = remainingAmount <= 0.0

    val pendingDays: Int
        get() {
            val diffMs = System.currentTimeMillis() - createdAt
            return (diffMs / (1000L * 60 * 60 * 24)).toInt().coerceAtLeast(0)
        }
}
