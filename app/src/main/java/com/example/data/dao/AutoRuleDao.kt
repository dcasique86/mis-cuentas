package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.AutoRuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AutoRuleDao {
    @Query("SELECT * FROM auto_rules ORDER BY id ASC")
    fun getAllRules(): Flow<List<AutoRuleEntity>>

    @Query("SELECT * FROM auto_rules WHERE isActive = 1")
    fun getActiveRules(): Flow<List<AutoRuleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRule(rule: AutoRuleEntity): Long

    @Update
    suspend fun updateRule(rule: AutoRuleEntity)

    @Delete
    suspend fun deleteRule(rule: AutoRuleEntity)

    @Query("DELETE FROM auto_rules")
    suspend fun clearAll()
}
