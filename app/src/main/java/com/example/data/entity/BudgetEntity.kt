package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String, // Comida, Transporte, Hogar, etc.
    val monthlyLimit: Double,
    val monthYearKey: String, // e.g. "2026-09" o "DEFAULT"
    val note: String = ""
)
