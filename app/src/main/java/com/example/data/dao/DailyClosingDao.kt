package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entity.DailyClosingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyClosingDao {
    @Query("SELECT * FROM daily_closings ORDER BY timestamp DESC")
    fun getAllClosings(): Flow<List<DailyClosingEntity>>

    @Query("SELECT * FROM daily_closings WHERE dateKey = :dateKey LIMIT 1")
    suspend fun getClosingByDate(dateKey: String): DailyClosingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClosing(closing: DailyClosingEntity): Long

    @Delete
    suspend fun deleteClosing(closing: DailyClosingEntity)

    @Query("DELETE FROM daily_closings")
    suspend fun clearAll()
}
