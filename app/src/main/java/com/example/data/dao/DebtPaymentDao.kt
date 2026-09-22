package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entity.DebtPaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DebtPaymentDao {
    @Query("SELECT * FROM debt_payments WHERE debtId = :debtId ORDER BY timestamp ASC")
    fun getPaymentsForDebt(debtId: Long): Flow<List<DebtPaymentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: DebtPaymentEntity): Long

    @Delete
    suspend fun deletePayment(payment: DebtPaymentEntity)

    @Query("DELETE FROM debt_payments WHERE debtId = :debtId")
    suspend fun deleteByDebtId(debtId: Long)

    @Query("DELETE FROM debt_payments")
    suspend fun clearAll()
}
