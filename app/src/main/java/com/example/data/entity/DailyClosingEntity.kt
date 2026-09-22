package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_closings")
data class DailyClosingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dateKey: String, // e.g. "2026-09-21"
    val timestamp: Long,
    val totalIncome: Double,
    val totalExpense: Double,
    val balance: Double,
    val expectedCash: Double,
    val actualCash: Double,
    val difference: Double,
    val incomeCount: Int,
    val expenseCount: Int,
    val totalMovements: Int,
    val status: String, // "CUADRADA", "FALTANTE", "SOBRANTE"
    val note: String = ""
)
