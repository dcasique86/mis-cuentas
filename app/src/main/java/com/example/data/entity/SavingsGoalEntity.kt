package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savings_goals")
data class SavingsGoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val targetAmount: Double,
    val savedAmount: Double = 0.0,
    val targetDate: Long? = null,
    val iconName: String = "flight", // flight, laptop, shield, gift, car, home, star
    val colorHex: Long = 0xFF35858B,
    val note: String = "",
    val isCompleted: Boolean = false
)
