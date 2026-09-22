package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tipos contables soportados en Mis Cuentas:
 * VENTA, INGRESO, GASTO, PAGO_DEUDA, COBRO_DEUDA, PRESTAMO_DADO, PRESTAMO_RECIBIDO,
 * TRANSFERENCIA, RETIRO_PERSONAL, APORTE, AJUSTE_CAJA, OTRO
 */
enum class AccountingType(val displayName: String, val isExpenseDefault: Boolean) {
    VENTA("Venta", false),
    INGRESO("Ingreso", false),
    GASTO("Gasto", true),
    PAGO_DEUDA("Pago de deuda", true),
    COBRO_DEUDA("Cobro de deuda", false),
    PRESTAMO_DADO("Préstamo dado", true),
    PRESTAMO_RECIBIDO("Préstamo recibido", false),
    TRANSFERENCIA("Transferencia", false),
    RETIRO_PERSONAL("Retiro personal", true),
    APORTE("Aporte de capital", false),
    AJUSTE_CAJA("Ajuste de caja", false),
    OTRO("Otro", true)
}

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String, // Value of AccountingType.name
    val concept: String,
    val category: String,
    val paymentMethod: String, // Efectivo, Transferencia, Tarjeta, etc.
    val amount: Double,
    val isIncome: Boolean, // true = suma al saldo, false = resta del saldo
    val timestamp: Long,
    val note: String = "",
    val relatedDebtId: Long? = null
)
