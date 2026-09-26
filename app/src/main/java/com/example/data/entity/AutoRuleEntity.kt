package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auto_rules")
data class AutoRuleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val keyword: String,
    val targetCategory: String,
    val isIncome: Boolean = false,
    val isActive: Boolean = true
)
