package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String, // e.g. "Almuerzo", "Transporte", "Envío", "Venta"
    val type: String, // Value of AccountingType.name (e.g. GASTO, VENTA, INGRESO)
    val category: String, // Comida, Transporte, etc.
    val paymentMethod: String, // Efectivo, Transferencia, etc.
    val amount: Double? = null, // Monto opcional
    val isIncome: Boolean = false
)
