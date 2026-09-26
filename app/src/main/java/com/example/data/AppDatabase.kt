package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.AccountDao
import com.example.data.dao.AutoRuleDao
import com.example.data.dao.BudgetDao
import com.example.data.dao.DailyClosingDao
import com.example.data.dao.DebtDao
import com.example.data.dao.DebtPaymentDao
import com.example.data.dao.FavoriteDao
import com.example.data.dao.RecurringTransactionDao
import com.example.data.dao.SavingsGoalDao
import com.example.data.dao.TransactionDao
import com.example.data.entity.AccountEntity
import com.example.data.entity.AutoRuleEntity
import com.example.data.entity.BudgetEntity
import com.example.data.entity.DailyClosingEntity
import com.example.data.entity.DebtEntity
import com.example.data.entity.DebtPaymentEntity
import com.example.data.entity.FavoriteEntity
import com.example.data.entity.RecurringTransactionEntity
import com.example.data.entity.SavingsGoalEntity
import com.example.data.entity.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        DebtEntity::class,
        FavoriteEntity::class,
        DebtPaymentEntity::class,
        DailyClosingEntity::class,
        AccountEntity::class,
        BudgetEntity::class,
        SavingsGoalEntity::class,
        RecurringTransactionEntity::class,
        AutoRuleEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun debtDao(): DebtDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun debtPaymentDao(): DebtPaymentDao
    abstract fun dailyClosingDao(): DailyClosingDao
    abstract fun accountDao(): AccountDao
    abstract fun budgetDao(): BudgetDao
    abstract fun savingsGoalDao(): SavingsGoalDao
    abstract fun recurringTransactionDao(): RecurringTransactionDao
    abstract fun autoRuleDao(): AutoRuleDao

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
