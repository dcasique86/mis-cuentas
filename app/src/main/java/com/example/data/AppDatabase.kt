package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.DailyClosingDao
import com.example.data.dao.DebtDao
import com.example.data.dao.DebtPaymentDao
import com.example.data.dao.FavoriteDao
import com.example.data.dao.TransactionDao
import com.example.data.entity.DailyClosingEntity
import com.example.data.entity.DebtEntity
import com.example.data.entity.DebtPaymentEntity
import com.example.data.entity.FavoriteEntity
import com.example.data.entity.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        DebtEntity::class,
        FavoriteEntity::class,
        DebtPaymentEntity::class,
        DailyClosingEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun debtDao(): DebtDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun debtPaymentDao(): DebtPaymentDao
    abstract fun dailyClosingDao(): DailyClosingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mis_cuentas_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
