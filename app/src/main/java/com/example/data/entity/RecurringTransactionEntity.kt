package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Periodicity(val displayName: String, val daysApprox: Int) {
    DAILY("Diario", 1),
    WEEKLY("Semanal", 7),
    BIWEEKLY("Quincenal", 15),
    MONTHLY("Mensual", 30),
    YEARLY("Anual", 365)
}

@Entity(tableName = "recurring_transactions")
data class RecurringTransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val description: String,
    val amount: Double,
    val isIncome: Boolean, // false = gasto (suscripción), true = ingreso recurrente
    val category: String,
    val periodicity: String = Periodicity.MONTHLY.name,
    val nextDueDate: Long, // timestamp in ms
    val paymentMethod: String = "Efectivo",
    val isActive: Boolean = true,
    val autoApply: Boolean = false,
    val lastAppliedDate: Long? = null
)
