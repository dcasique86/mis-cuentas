package com.example.data.repository

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
import com.example.data.entity.AccountingType
import com.example.data.entity.AutoRuleEntity
import com.example.data.entity.BudgetEntity
import com.example.data.entity.DailyClosingEntity
import com.example.data.entity.DebtEntity
import com.example.data.entity.DebtPaymentEntity
import com.example.data.entity.FavoriteEntity
import com.example.data.entity.Periodicity
import com.example.data.entity.RecurringTransactionEntity
import com.example.data.entity.SavingsGoalEntity
import com.example.data.entity.TransactionEntity
import com.example.util.VoiceInputParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FinanceRepository(
    private val transactionDao: TransactionDao,
    private val debtDao: DebtDao,
    private val favoriteDao: FavoriteDao,
    private val debtPaymentDao: DebtPaymentDao,
    private val dailyClosingDao: DailyClosingDao,
    private val accountDao: AccountDao,
    private val budgetDao: BudgetDao,
    private val savingsGoalDao: SavingsGoalDao,
    private val recurringTransactionDao: RecurringTransactionDao,
    private val autoRuleDao: AutoRuleDao
) {
    val allTransactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()
    val recentTransactions: Flow<List<TransactionEntity>> = transactionDao.getRecentTransactions(6)
    val allDebts: Flow<List<DebtEntity>> = debtDao.getAllDebts()
    val allFavorites: Flow<List<FavoriteEntity>> = favoriteDao.getAllFavorites()
    val allClosings: Flow<List<DailyClosingEntity>> = dailyClosingDao.getAllClosings()
    val allAccounts: Flow<List<AccountEntity>> = accountDao.getAllAccounts()
    val allBudgets: Flow<List<BudgetEntity>> = budgetDao.getAllBudgets()
    val allGoals: Flow<List<SavingsGoalEntity>> = savingsGoalDao.getAllGoals()
    val allRecurring: Flow<List<RecurringTransactionEntity>> = recurringTransactionDao.getAllRecurring()
    val activeRecurring: Flow<List<RecurringTransactionEntity>> = recurringTransactionDao.getActiveRecurring()
    val allRules: Flow<List<AutoRuleEntity>> = autoRuleDao.getAllRules()
    val activeRules: Flow<List<AutoRuleEntity>> = autoRuleDao.getActiveRules()

    fun getBudgetsForMonth(monthKey: String): Flow<List<BudgetEntity>> =
        budgetDao.getBudgetsForMonth(monthKey)

    suspend fun insertAccount(account: AccountEntity): Long = withContext(Dispatchers.IO) {
        accountDao.insertAccount(account)
    }

    suspend fun updateAccount(account: AccountEntity) = withContext(Dispatchers.IO) {
        accountDao.updateAccount(account)
    }

    suspend fun deleteAccount(account: AccountEntity) = withContext(Dispatchers.IO) {
        accountDao.deleteAccount(account)
    }

    suspend fun insertBudget(budget: BudgetEntity): Long = withContext(Dispatchers.IO) {
        budgetDao.insertBudget(budget)
    }

    suspend fun updateBudget(budget: BudgetEntity) = withContext(Dispatchers.IO) {
        budgetDao.updateBudget(budget)
    }

    suspend fun deleteBudget(budget: BudgetEntity) = withContext(Dispatchers.IO) {
        budgetDao.deleteBudget(budget)
    }

    suspend fun insertGoal(goal: SavingsGoalEntity): Long = withContext(Dispatchers.IO) {
        savingsGoalDao.insertGoal(goal)
    }

    suspend fun updateGoal(goal: SavingsGoalEntity) = withContext(Dispatchers.IO) {
        savingsGoalDao.updateGoal(goal)
    }

    suspend fun deleteGoal(goal: SavingsGoalEntity) = withContext(Dispatchers.IO) {
        savingsGoalDao.deleteGoal(goal)
    }

    // Recurring Transactions methods
    suspend fun insertRecurring(recurring: RecurringTransactionEntity): Long = withContext(Dispatchers.IO) {
        recurringTransactionDao.insertRecurring(recurring)
    }

    suspend fun updateRecurring(recurring: RecurringTransactionEntity) = withContext(Dispatchers.IO) {
        recurringTransactionDao.updateRecurring(recurring)
    }

    suspend fun deleteRecurring(recurring: RecurringTransactionEntity) = withContext(Dispatchers.IO) {
        recurringTransactionDao.deleteRecurring(recurring)
    }

    fun calculateNextDueDate(currentDue: Long, periodicity: String): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = if (currentDue > 0) currentDue else System.currentTimeMillis()
        when (periodicity.uppercase()) {
            Periodicity.DAILY.name -> cal.add(Calendar.DAY_OF_YEAR, 1)
            Periodicity.WEEKLY.name -> cal.add(Calendar.WEEK_OF_YEAR, 1)
            Periodicity.BIWEEKLY.name -> cal.add(Calendar.DAY_OF_YEAR, 15)
            Periodicity.MONTHLY.name -> cal.add(Calendar.MONTH, 1)
            Periodicity.YEARLY.name -> cal.add(Calendar.YEAR, 1)
            else -> cal.add(Calendar.MONTH, 1)
        }
        return cal.timeInMillis
    }

    suspend fun applyRecurringNow(recurring: RecurringTransactionEntity): Long = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        val type = if (recurring.isIncome) AccountingType.INGRESO.name else AccountingType.GASTO.name
        val tx = TransactionEntity(
            type = type,
            concept = recurring.description,
            category = recurring.category,
            paymentMethod = recurring.paymentMethod,
            amount = recurring.amount,
            isIncome = recurring.isIncome,
            timestamp = now,
            note = "Recurrente: ${recurring.periodicity}"
        )
        val txId = transactionDao.insertTransaction(tx)

        val nextDue = calculateNextDueDate(recurring.nextDueDate, recurring.periodicity)
        recurringTransactionDao.updateRecurring(
            recurring.copy(
                nextDueDate = nextDue,
                lastAppliedDate = now
            )
        )
        txId
    }

    // Auto Rules methods
    suspend fun insertRule(rule: AutoRuleEntity): Long = withContext(Dispatchers.IO) {
        autoRuleDao.insertRule(rule)
    }

    suspend fun updateRule(rule: AutoRuleEntity) = withContext(Dispatchers.IO) {
        autoRuleDao.updateRule(rule)
    }

    suspend fun deleteRule(rule: AutoRuleEntity) = withContext(Dispatchers.IO) {
        autoRuleDao.deleteRule(rule)
    }

    fun getPaymentsForDebt(debtId: Long): Flow<List<DebtPaymentEntity>> =
        debtPaymentDao.getPaymentsForDebt(debtId)

    suspend fun insertTransaction(transaction: TransactionEntity): Long = withContext(Dispatchers.IO) {
        transactionDao.insertTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: TransactionEntity) = withContext(Dispatchers.IO) {
        transactionDao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: TransactionEntity) = withContext(Dispatchers.IO) {
        transactionDao.deleteTransaction(transaction)
    }

    suspend fun insertDebt(debt: DebtEntity, registerCashMovement: Boolean = false): Long = withContext(Dispatchers.IO) {
        val debtId = debtDao.insertDebt(debt)

        // Record initial loan event in history
        debtPaymentDao.insertPayment(
            DebtPaymentEntity(
                debtId = debtId,
                amount = debt.totalAmount,
                paymentMethod = "Efectivo",
                timestamp = debt.createdAt,
                note = if (debt.isIOwe) "Préstamo recibido" else "Préstamo entregado",
                isInitialLoan = true
            )
        )

        // If requested, record cash movement according to accounting rules:
        // Por cobrar (presté dinero) -> Sale de efectivo (isIncome = false, PRESTAMO_DADO)
        // Por pagar (me prestaron) -> Entra a efectivo (isIncome = true, PRESTAMO_RECIBIDO)
        if (registerCashMovement) {
            val type = if (debt.isIOwe) AccountingType.PRESTAMO_RECIBIDO.name else AccountingType.PRESTAMO_DADO.name
            val concept = if (debt.isIOwe) "Préstamo recibido de ${debt.name}" else "Préstamo dado a ${debt.name}"
            val isIncome = debt.isIOwe
            val tx = TransactionEntity(
                type = type,
                concept = concept,
                category = "Préstamo",
                paymentMethod = "Efectivo",
                amount = debt.totalAmount,
                isIncome = isIncome,
                timestamp = debt.createdAt,
                note = debt.note,
                relatedDebtId = debtId
            )
            transactionDao.insertTransaction(tx)
        }

        debtId
    }

    suspend fun updateDebt(debt: DebtEntity) = withContext(Dispatchers.IO) {
        debtDao.updateDebt(debt)
    }

    suspend fun deleteDebt(debt: DebtEntity) = withContext(Dispatchers.IO) {
        debtPaymentDao.deleteByDebtId(debt.id)
        debtDao.deleteDebt(debt)
    }

    suspend fun recordDebtPayment(
        debtId: Long,
        amount: Double,
        method: String,
        note: String
    ) = withContext(Dispatchers.IO) {
        val debt = debtDao.getDebtById(debtId) ?: return@withContext
        val now = System.currentTimeMillis()

        // Update debt amounts and last payment date
        val updatedDebt = debt.copy(
            paidAmount = debt.paidAmount + amount,
            lastPaymentDate = now
        )
        debtDao.updateDebt(updatedDebt)

        // Record payment in debt payment history
        debtPaymentDao.insertPayment(
            DebtPaymentEntity(
                debtId = debtId,
                amount = amount,
                paymentMethod = method,
                timestamp = now,
                note = note,
                isInitialLoan = false
            )
        )

        // Record transaction in cash history
        // Por pagar (yo debía) -> Pago de deuda: sale dinero (isIncome = false)
        // Por cobrar (me debían) -> Cobro de deuda: entra dinero (isIncome = true)
        val type = if (debt.isIOwe) AccountingType.PAGO_DEUDA.name else AccountingType.COBRO_DEUDA.name
        val concept = if (debt.isIOwe) "Abono a: ${debt.name}" else "Cobro de: ${debt.name}"
        val isIncome = !debt.isIOwe

        val transaction = TransactionEntity(
            type = type,
            concept = concept,
            category = "Deudas",
            paymentMethod = method,
            amount = amount,
            isIncome = isIncome,
            timestamp = now,
            note = note,
            relatedDebtId = debtId
        )
        transactionDao.insertTransaction(transaction)
    }

    // Favorites
    suspend fun insertFavorite(favorite: FavoriteEntity): Long = withContext(Dispatchers.IO) {
        favoriteDao.insertFavorite(favorite)
    }

    suspend fun updateFavorite(favorite: FavoriteEntity) = withContext(Dispatchers.IO) {
        favoriteDao.updateFavorite(favorite)
    }

    suspend fun deleteFavorite(favorite: FavoriteEntity) = withContext(Dispatchers.IO) {
        favoriteDao.deleteFavorite(favorite)
    }

    // Daily Closings
    suspend fun insertClosing(closing: DailyClosingEntity): Long = withContext(Dispatchers.IO) {
        dailyClosingDao.insertClosing(closing)
    }

    suspend fun deleteClosing(closing: DailyClosingEntity) = withContext(Dispatchers.IO) {
        dailyClosingDao.deleteClosing(closing)
    }

    suspend fun clearDatabaseForRealTesting() = withContext(Dispatchers.IO) {
        transactionDao.clearAll()
        debtDao.clearAll()
        debtPaymentDao.clearAll()
        dailyClosingDao.clearAll()
        // Ensure standard favorite shortcuts exist for testing convenience
        val favs = favoriteDao.getAllFavorites().first()
        if (favs.isEmpty()) {
            val defaultFavs = listOf(
                FavoriteEntity(name = "Almuerzo", type = AccountingType.GASTO.name, category = "Comida", paymentMethod = "Efectivo", amount = 15000.0, isIncome = false),
                FavoriteEntity(name = "Transporte", type = AccountingType.GASTO.name, category = "Transporte", paymentMethod = "Efectivo", amount = 10000.0, isIncome = false),
                FavoriteEntity(name = "Envío", type = AccountingType.GASTO.name, category = "Transporte", paymentMethod = "Efectivo", amount = 12500.0, isIncome = false),
                FavoriteEntity(name = "Venta", type = AccountingType.VENTA.name, category = "Venta", paymentMethod = "Efectivo", amount = null, isIncome = true)
            )
            defaultFavs.forEach { favoriteDao.insertFavorite(it) }
        }
    }

    suspend fun seedInitialDataIfEmpty() = withContext(Dispatchers.IO) {
        // Only seed default favorite templates if empty, NO mock transactions for real testing
        val favs = favoriteDao.getAllFavorites().first()
        if (favs.isEmpty()) {
            val defaultFavs = listOf(
                FavoriteEntity(name = "Almuerzo", type = AccountingType.GASTO.name, category = "Comida", paymentMethod = "Efectivo", amount = 15000.0, isIncome = false),
                FavoriteEntity(name = "Transporte", type = AccountingType.GASTO.name, category = "Transporte", paymentMethod = "Efectivo", amount = 10000.0, isIncome = false),
                FavoriteEntity(name = "Envío", type = AccountingType.GASTO.name, category = "Transporte", paymentMethod = "Efectivo", amount = 12500.0, isIncome = false),
                FavoriteEntity(name = "Venta", type = AccountingType.VENTA.name, category = "Venta", paymentMethod = "Efectivo", amount = null, isIncome = true)
            )
            defaultFavs.forEach { favoriteDao.insertFavorite(it) }
        }

        // Initialize default accounts matching design if empty
        val accounts = accountDao.getAllAccounts().first()
        if (accounts.isEmpty()) {
            val defaultAccounts = listOf(
                AccountEntity(name = "Efectivo", type = "CASH", currentBalance = 350000.0, iconName = "cash"),
                AccountEntity(name = "Nequi", type = "WALLET", currentBalance = 280000.0, iconName = "nequi"),
                AccountEntity(name = "Bancolombia", type = "BANK", currentBalance = 1250000.0, iconName = "bank"),
                AccountEntity(name = "Davivienda", type = "BANK", currentBalance = 420000.0, iconName = "bank"),
                AccountEntity(name = "Tarjeta de crédito", type = "CREDIT_CARD", currentBalance = 980000.0, creditLimit = 2000000.0, iconName = "credit_card"),
                AccountEntity(name = "Ahorro", type = "SAVINGS", currentBalance = 600000.0, iconName = "savings")
            )
            defaultAccounts.forEach { accountDao.insertAccount(it) }
        }

        // Initialize default budgets matching design if empty
        val budgets = budgetDao.getAllBudgets().first()
        if (budgets.isEmpty()) {
            val defaultBudgets = listOf(
                BudgetEntity(category = "Comida", monthlyLimit = 400000.0, monthYearKey = "DEFAULT"),
                BudgetEntity(category = "Transporte", monthlyLimit = 200000.0, monthYearKey = "DEFAULT"),
                BudgetEntity(category = "Hogar", monthlyLimit = 500000.0, monthYearKey = "DEFAULT"),
                BudgetEntity(category = "Entretenimiento", monthlyLimit = 200000.0, monthYearKey = "DEFAULT"),
                BudgetEntity(category = "Salud", monthlyLimit = 150000.0, monthYearKey = "DEFAULT")
            )
            defaultBudgets.forEach { budgetDao.insertBudget(it) }
        }

        // Initialize default savings goals matching design if empty
        val goals = savingsGoalDao.getAllGoals().first()
        if (goals.isEmpty()) {
            val defaultGoals = listOf(
                SavingsGoalEntity(name = "Viaje a México", targetAmount = 1500000.0, savedAmount = 750000.0, iconName = "flight"),
                SavingsGoalEntity(name = "PC nueva", targetAmount = 3000000.0, savedAmount = 1200000.0, iconName = "laptop"),
                SavingsGoalEntity(name = "Emergencias", targetAmount = 2000000.0, savedAmount = 600000.0, iconName = "shield"),
                SavingsGoalEntity(name = "Navidad", targetAmount = 1000000.0, savedAmount = 300000.0, iconName = "gift")
            )
            defaultGoals.forEach { savingsGoalDao.insertGoal(it) }
        }

        // Initialize default recurring subscriptions if empty
        val recurring = recurringTransactionDao.getAllRecurring().first()
        if (recurring.isEmpty()) {
            val now = System.currentTimeMillis()
            val dayMs = 24L * 3600L * 1000L
            val defaultRecurring = listOf(
                RecurringTransactionEntity(description = "Netflix", amount = 19900.0, isIncome = false, category = "Entretenimiento", periodicity = Periodicity.MONTHLY.name, nextDueDate = now + (4L * dayMs), paymentMethod = "Tarjeta de crédito"),
                RecurringTransactionEntity(description = "Spotify Familiar", amount = 24900.0, isIncome = false, category = "Entretenimiento", periodicity = Periodicity.MONTHLY.name, nextDueDate = now + (11L * dayMs), paymentMethod = "Nequi"),
                RecurringTransactionEntity(description = "Internet Claro Fibra", amount = 85000.0, isIncome = false, category = "Hogar", periodicity = Periodicity.MONTHLY.name, nextDueDate = now + (18L * dayMs), paymentMethod = "Bancolombia"),
                RecurringTransactionEntity(description = "Salario Quincenal", amount = 1500000.0, isIncome = true, category = "Trabajo", periodicity = Periodicity.BIWEEKLY.name, nextDueDate = now + (6L * dayMs), paymentMethod = "Bancolombia")
            )
            defaultRecurring.forEach { recurringTransactionDao.insertRecurring(it) }
        }

        // Initialize default auto categorization rules if empty
        val rules = autoRuleDao.getAllRules().first()
        if (rules.isEmpty()) {
            for (rule in VoiceInputParser.getDefaultRules()) {
                autoRuleDao.insertRule(rule)
            }
        }
    }

    suspend fun seedDemoData() = withContext(Dispatchers.IO) {
        val existing = transactionDao.getAllTransactions().first()
        if (existing.isNotEmpty()) return@withContext

        val now = System.currentTimeMillis()
        val dayMs = 24L * 3600L * 1000L
        val cal = Calendar.getInstance()

        // 1. Initial base cash
        cal.timeInMillis = now
        cal.add(Calendar.DAY_OF_YEAR, -2)
        val initialAporte = TransactionEntity(
            type = AccountingType.APORTE.name,
            concept = "Saldo inicial de caja",
            category = "Aporte",
            paymentMethod = "Efectivo",
            amount = 12850.0,
            isIncome = true,
            timestamp = cal.timeInMillis,
            note = "Base de caja"
        )
        transactionDao.insertTransaction(initialAporte)

        // 2. Yesterday movement
        cal.timeInMillis = now
        cal.add(Calendar.DAY_OF_YEAR, -1)
        cal.set(Calendar.HOUR_OF_DAY, 18)
        val txYesterday = TransactionEntity(
            type = AccountingType.VENTA.name,
            concept = "Venta chaqueta",
            category = "Venta",
            paymentMethod = "Transferencia",
            amount = 120.0,
            isIncome = true,
            timestamp = cal.timeInMillis
        )
        transactionDao.insertTransaction(txYesterday)

        // 3. Today's movements:
        cal.timeInMillis = now
        cal.set(Calendar.HOUR_OF_DAY, 8)
        cal.set(Calendar.MINUTE, 1)
        val tx1 = TransactionEntity(
            type = AccountingType.GASTO.name,
            concept = "Chuzo",
            category = "Comida",
            paymentMethod = "Efectivo",
            amount = 10.0,
            isIncome = false,
            timestamp = cal.timeInMillis
        )
        transactionDao.insertTransaction(tx1)

        cal.set(Calendar.HOUR_OF_DAY, 8)
        cal.set(Calendar.MINUTE, 15)
        val tx2 = TransactionEntity(
            type = AccountingType.GASTO.name,
            concept = "Envío",
            category = "Transporte",
            paymentMethod = "Efectivo",
            amount = 590.0,
            isIncome = false,
            timestamp = cal.timeInMillis
        )
        transactionDao.insertTransaction(tx2)

        cal.set(Calendar.HOUR_OF_DAY, 9)
        cal.set(Calendar.MINUTE, 20)
        val tx3 = TransactionEntity(
            type = AccountingType.PRESTAMO_RECIBIDO.name,
            concept = "Préstamo",
            category = "Préstamo",
            paymentMethod = "Transferencia",
            amount = 940.0,
            isIncome = true,
            timestamp = cal.timeInMillis
        )
        transactionDao.insertTransaction(tx3)

        cal.set(Calendar.HOUR_OF_DAY, 10)
        cal.set(Calendar.MINUTE, 3)
        val tx4 = TransactionEntity(
            type = AccountingType.VENTA.name,
            concept = "Licra",
            category = "Venta",
            paymentMethod = "Efectivo",
            amount = 170.0,
            isIncome = true,
            timestamp = cal.timeInMillis
        )
        transactionDao.insertTransaction(tx4)

        cal.set(Calendar.HOUR_OF_DAY, 11)
        cal.set(Calendar.MINUTE, 24)
        val tx5 = TransactionEntity(
            type = AccountingType.VENTA.name,
            concept = "Venta pantaloneta",
            category = "Venta",
            paymentMethod = "Efectivo",
            amount = 140.0,
            isIncome = true,
            timestamp = cal.timeInMillis
        )
        transactionDao.insertTransaction(tx5)

        // Seed Debts with realistic timestamps and payment history
        val calDate = Calendar.getInstance()

        // Carlos: Debe 750.000, pagado 200.000, pendiente 550.000
        calDate.timeInMillis = now - (18L * dayMs)
        val debt1 = DebtEntity(
            name = "Carlos",
            totalAmount = 750000.0,
            paidAmount = 200000.0,
            isIOwe = false, // "Me deben" / Por cobrar
            createdAt = calDate.timeInMillis,
            lastPaymentDate = now - (6L * dayMs),
            note = "Préstamo compra mercancía"
        )
        val id1 = debtDao.insertDebt(debt1)
        debtPaymentDao.insertPayment(
            DebtPaymentEntity(debtId = id1, amount = 750000.0, paymentMethod = "Efectivo", timestamp = calDate.timeInMillis, note = "Préstamo", isInitialLoan = true)
        )
        debtPaymentDao.insertPayment(
            DebtPaymentEntity(debtId = id1, amount = 100000.0, paymentMethod = "Efectivo", timestamp = now - (12L * dayMs), note = "Abono 1", isInitialLoan = false)
        )
        debtPaymentDao.insertPayment(
            DebtPaymentEntity(debtId = id1, amount = 100000.0, paymentMethod = "Transferencia", timestamp = now - (6L * dayMs), note = "Abono 2", isInitialLoan = false)
        )

        // Daniel: Por pagar 360.000, pagado 100.000, pendiente 260.000
        calDate.timeInMillis = now - (10L * dayMs)
        val debt2 = DebtEntity(
            name = "Daniel",
            totalAmount = 360000.0,
            paidAmount = 100000.0,
            isIOwe = true,
            createdAt = calDate.timeInMillis,
            lastPaymentDate = now - (3L * dayMs),
            note = "Arreglo moto"
        )
        val id2 = debtDao.insertDebt(debt2)
        debtPaymentDao.insertPayment(
            DebtPaymentEntity(debtId = id2, amount = 360000.0, paymentMethod = "Efectivo", timestamp = calDate.timeInMillis, note = "Préstamo taller", isInitialLoan = true)
        )
        debtPaymentDao.insertPayment(
            DebtPaymentEntity(debtId = id2, amount = 100000.0, paymentMethod = "Efectivo", timestamp = now - (3L * dayMs), note = "Abono taller", isInitialLoan = false)
        )

        // Tarjeta de crédito: Por pagar 900.000
        calDate.timeInMillis = now - (5L * dayMs)
        val debt3 = DebtEntity(
            name = "Tarjeta de crédito",
            totalAmount = 900000.0,
            paidAmount = 0.0,
            isIOwe = true,
            createdAt = calDate.timeInMillis,
            note = "Cuota compra electrodomésticos"
        )
        val id3 = debtDao.insertDebt(debt3)
        debtPaymentDao.insertPayment(
            DebtPaymentEntity(debtId = id3, amount = 900000.0, paymentMethod = "Tarjeta", timestamp = calDate.timeInMillis, note = "Compra diferida", isInitialLoan = true)
        )

        // Andrés: Por cobrar 180.000, pagado 50.000, pendiente 130.000
        calDate.timeInMillis = now - (14L * dayMs)
        val debt4 = DebtEntity(
            name = "Andrés",
            totalAmount = 180000.0,
            paidAmount = 50000.0,
            isIOwe = false, // Me debe
            createdAt = calDate.timeInMillis,
            lastPaymentDate = now - (4L * dayMs),
            note = "Venta por cuotas"
        )
        val id4 = debtDao.insertDebt(debt4)
        debtPaymentDao.insertPayment(
            DebtPaymentEntity(debtId = id4, amount = 180000.0, paymentMethod = "Efectivo", timestamp = calDate.timeInMillis, note = "Venta a crédito", isInitialLoan = true)
        )
        debtPaymentDao.insertPayment(
            DebtPaymentEntity(debtId = id4, amount = 50000.0, paymentMethod = "Nequi", timestamp = now - (4L * dayMs), note = "Abono inicial", isInitialLoan = false)
        )

        // Camilo: Por cobrar 450.000, pagado 200.000, pendiente 250.000
        calDate.timeInMillis = now - (25L * dayMs)
        val debt5 = DebtEntity(
            name = "Camilo",
            totalAmount = 450000.0,
            paidAmount = 200000.0,
            isIOwe = false, // Me debe
            createdAt = calDate.timeInMillis,
            lastPaymentDate = now - (8L * dayMs),
            note = "Préstamo personal"
        )
        val id5 = debtDao.insertDebt(debt5)
        debtPaymentDao.insertPayment(
            DebtPaymentEntity(debtId = id5, amount = 450000.0, paymentMethod = "Efectivo", timestamp = calDate.timeInMillis, note = "Préstamo personal", isInitialLoan = true)
        )
        debtPaymentDao.insertPayment(
            DebtPaymentEntity(debtId = id5, amount = 200000.0, paymentMethod = "Efectivo", timestamp = now - (8L * dayMs), note = "Abono parcial", isInitialLoan = false)
        )

        // Seed initial favorites according to requirement 2
        val favs = listOf(
            FavoriteEntity(name = "Almuerzo", type = AccountingType.GASTO.name, category = "Comida", paymentMethod = "Efectivo", amount = 15000.0, isIncome = false),
            FavoriteEntity(name = "Transporte", type = AccountingType.GASTO.name, category = "Transporte", paymentMethod = "Efectivo", amount = 10000.0, isIncome = false),
            FavoriteEntity(name = "Envío", type = AccountingType.GASTO.name, category = "Transporte", paymentMethod = "Efectivo", amount = 12500.0, isIncome = false),
            FavoriteEntity(name = "Venta", type = AccountingType.VENTA.name, category = "Venta", paymentMethod = "Efectivo", amount = null, isIncome = true),
            FavoriteEntity(name = "Mercado", type = AccountingType.GASTO.name, category = "Compras", paymentMethod = "Efectivo", amount = 50000.0, isIncome = false),
            FavoriteEntity(name = "Recarga", type = AccountingType.GASTO.name, category = "Servicios", paymentMethod = "Transferencia", amount = 10000.0, isIncome = false)
        )
        favs.forEach { favoriteDao.insertFavorite(it) }

        // Seed past daily closings according to requirement 4
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val closingYesterday = DailyClosingEntity(
            dateKey = sdf.format(now - dayMs),
            timestamp = now - dayMs,
            totalIncome = 850000.0,
            totalExpense = 320000.0,
            balance = 530000.0,
            expectedCash = 12850.0,
            actualCash = 12850.0,
            difference = 0.0,
            incomeCount = 4,
            expenseCount = 3,
            totalMovements = 7,
            status = "CUADRADA",
            note = "Cierre sin novedades"
        )
        val closingDayBefore = DailyClosingEntity(
            dateKey = sdf.format(now - (2L * dayMs)),
            timestamp = now - (2L * dayMs),
            totalIncome = 620000.0,
            totalExpense = 410000.0,
            balance = 210000.0,
            expectedCash = 12640.0,
            actualCash = 12620.0,
            difference = -20.0,
            incomeCount = 3,
            expenseCount = 4,
            totalMovements = 7,
            status = "FALTANTE",
            note = "Faltante menor por cambio"
        )
        dailyClosingDao.insertClosing(closingYesterday)
        dailyClosingDao.insertClosing(closingDayBefore)

        // Seed initial accounts from the reference design
        val initialAccounts = listOf(
            AccountEntity(name = "Efectivo", type = "CASH", currentBalance = 350000.0, iconName = "cash"),
            AccountEntity(name = "Nequi", type = "WALLET", currentBalance = 280000.0, iconName = "nequi"),
            AccountEntity(name = "Bancolombia", type = "BANK", currentBalance = 1250000.0, iconName = "bank"),
            AccountEntity(name = "Davivienda", type = "BANK", currentBalance = 420000.0, iconName = "bank"),
            AccountEntity(name = "Tarjeta de crédito", type = "CREDIT_CARD", currentBalance = 980000.0, creditLimit = 2000000.0, iconName = "credit_card"),
            AccountEntity(name = "Ahorro", type = "SAVINGS", currentBalance = 600000.0, iconName = "savings")
        )
        initialAccounts.forEach { accountDao.insertAccount(it) }

        // Seed initial budgets from the reference design
        val initialBudgets = listOf(
            BudgetEntity(category = "Comida", monthlyLimit = 400000.0, monthYearKey = "DEFAULT"),
            BudgetEntity(category = "Transporte", monthlyLimit = 200000.0, monthYearKey = "DEFAULT"),
            BudgetEntity(category = "Hogar", monthlyLimit = 500000.0, monthYearKey = "DEFAULT"),
            BudgetEntity(category = "Entretenimiento", monthlyLimit = 200000.0, monthYearKey = "DEFAULT"),
            BudgetEntity(category = "Salud", monthlyLimit = 150000.0, monthYearKey = "DEFAULT")
        )
        initialBudgets.forEach { budgetDao.insertBudget(it) }

        // Seed initial savings goals from the reference design
        val initialGoals = listOf(
            SavingsGoalEntity(name = "Viaje a México", targetAmount = 1500000.0, savedAmount = 750000.0, iconName = "flight"),
            SavingsGoalEntity(name = "PC nueva", targetAmount = 3000000.0, savedAmount = 1200000.0, iconName = "laptop"),
            SavingsGoalEntity(name = "Emergencias", targetAmount = 2000000.0, savedAmount = 600000.0, iconName = "shield"),
            SavingsGoalEntity(name = "Navidad", targetAmount = 1000000.0, savedAmount = 300000.0, iconName = "gift")
        )
        initialGoals.forEach { savingsGoalDao.insertGoal(it) }

        // Seed initial recurring
        val initialRecurring = listOf(
            RecurringTransactionEntity(description = "Netflix", amount = 19900.0, isIncome = false, category = "Entretenimiento", periodicity = Periodicity.MONTHLY.name, nextDueDate = now + (4L * dayMs), paymentMethod = "Tarjeta de crédito"),
            RecurringTransactionEntity(description = "Spotify Familiar", amount = 24900.0, isIncome = false, category = "Entretenimiento", periodicity = Periodicity.MONTHLY.name, nextDueDate = now + (11L * dayMs), paymentMethod = "Nequi"),
            RecurringTransactionEntity(description = "Internet Claro Fibra", amount = 85000.0, isIncome = false, category = "Hogar", periodicity = Periodicity.MONTHLY.name, nextDueDate = now + (18L * dayMs), paymentMethod = "Bancolombia"),
            RecurringTransactionEntity(description = "Salario Quincenal", amount = 1500000.0, isIncome = true, category = "Trabajo", periodicity = Periodicity.BIWEEKLY.name, nextDueDate = now + (6L * dayMs), paymentMethod = "Bancolombia")
        )
        initialRecurring.forEach { recurringTransactionDao.insertRecurring(it) }

        // Seed initial rules
        for (rule in VoiceInputParser.getDefaultRules()) {
            autoRuleDao.insertRule(rule)
        }
    }

    suspend fun resetAllData() = withContext(Dispatchers.IO) {
        transactionDao.clearAll()
        debtDao.clearAll()
        favoriteDao.clearAll()
        debtPaymentDao.clearAll()
        dailyClosingDao.clearAll()
        accountDao.clearAll()
        budgetDao.clearAll()
        savingsGoalDao.clearAll()
        recurringTransactionDao.clearAll()
        autoRuleDao.clearAll()
        seedInitialDataIfEmpty()
    }
}
