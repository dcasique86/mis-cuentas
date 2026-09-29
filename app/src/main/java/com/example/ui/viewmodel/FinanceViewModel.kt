package com.example.ui.viewmodel

import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.entity.AccountingType
import com.example.data.entity.DailyClosingEntity
import com.example.data.entity.DebtEntity
import com.example.data.entity.DebtPaymentEntity
import com.example.data.entity.FavoriteEntity
import com.example.data.entity.TransactionEntity
import com.example.data.repository.FinanceRepository
import com.example.widget.MisCuentasWidgetProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class TodaySummary(
    val income: Double = 0.0,
    val expense: Double = 0.0,
    val balance: Double = 0.0,
    val incomeCount: Int = 0,
    val expenseCount: Int = 0,
    val totalCount: Int = 0
)

data class WeeklyBarData(
    val weekLabel: String,
    val income: Double,
    val expense: Double
)

data class MonthlyReportData(
    val monthName: String,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val balance: Double = 0.0,
    val currentCash: Double = 0.0,
    val dailyAverageExpense: Double = 0.0,
    val highestIncomeDay: String = "-",
    val highestExpenseDay: String = "-",
    val incomeByCategory: List<Pair<String, Double>> = emptyList(),
    val expenseByCategory: List<Pair<String, Double>> = emptyList(),
    val weeklyBreakdown: List<WeeklyBarData> = emptyList(),
    val prevMonthIncome: Double = 0.0,
    val prevMonthExpense: Double = 0.0,
    val incomeChangePercent: Double? = null,
    val expenseChangePercent: Double? = null
)

class FinanceViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FinanceRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = FinanceRepository(
            database.transactionDao(),
            database.debtDao(),
            database.favoriteDao(),
            database.debtPaymentDao(),
            database.dailyClosingDao(),
            database.accountDao(),
            database.budgetDao(),
            database.savingsGoalDao(),
            database.recurringTransactionDao(),
            database.autoRuleDao()
        )
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
            notifyWidgetUpdate()
        }
    }

    // Accounts, Budgets, Savings Goals
    val allAccounts: StateFlow<List<com.example.data.entity.AccountEntity>> = repository.allAccounts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBudgets: StateFlow<List<com.example.data.entity.BudgetEntity>> = repository.allBudgets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allGoals: StateFlow<List<com.example.data.entity.SavingsGoalEntity>> = repository.allGoals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRecurring: StateFlow<List<com.example.data.entity.RecurringTransactionEntity>> = repository.allRecurring
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeRecurring: StateFlow<List<com.example.data.entity.RecurringTransactionEntity>> = repository.activeRecurring
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRules: StateFlow<List<com.example.data.entity.AutoRuleEntity>> = repository.allRules
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeRules: StateFlow<List<com.example.data.entity.AutoRuleEntity>> = repository.activeRules
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addRecurring(recurring: com.example.data.entity.RecurringTransactionEntity) {
        viewModelScope.launch {
            repository.insertRecurring(recurring)
        }
    }

    fun updateRecurring(recurring: com.example.data.entity.RecurringTransactionEntity) {
        viewModelScope.launch {
            repository.updateRecurring(recurring)
        }
    }

    fun deleteRecurring(recurring: com.example.data.entity.RecurringTransactionEntity) {
        viewModelScope.launch {
            repository.deleteRecurring(recurring)
        }
    }

    fun applyRecurringNow(recurring: com.example.data.entity.RecurringTransactionEntity) {
        viewModelScope.launch {
            repository.applyRecurringNow(recurring)
            notifyWidgetUpdate()
        }
    }

    fun addRule(rule: com.example.data.entity.AutoRuleEntity) {
        viewModelScope.launch {
            repository.insertRule(rule)
        }
    }

    fun updateRule(rule: com.example.data.entity.AutoRuleEntity) {
        viewModelScope.launch {
            repository.updateRule(rule)
        }
    }

    fun deleteRule(rule: com.example.data.entity.AutoRuleEntity) {
        viewModelScope.launch {
            repository.deleteRule(rule)
        }
    }

    fun predictCategory(concept: String): com.example.data.entity.AutoRuleEntity? {
        return com.example.util.VoiceInputParser.matchCategory(concept, allRules.value)
    }

    fun registerVoiceTransaction(
        parsed: com.example.util.ParsedVoiceTransaction,
        paymentMethod: String = "Efectivo"
    ) {
        viewModelScope.launch {
            val type = if (parsed.isIncome) AccountingType.INGRESO.name else AccountingType.GASTO.name
            val tx = TransactionEntity(
                type = type,
                concept = parsed.concept.ifBlank { "Transacción rápida" },
                category = parsed.category,
                paymentMethod = paymentMethod,
                amount = parsed.amount,
                isIncome = parsed.isIncome,
                timestamp = System.currentTimeMillis(),
                note = "Voz / Dictado rápido"
            )
            repository.insertTransaction(tx)
            notifyWidgetUpdate()
        }
    }

    fun addAccount(account: com.example.data.entity.AccountEntity) {
        viewModelScope.launch {
            repository.insertAccount(account)
        }
    }

    fun updateAccount(account: com.example.data.entity.AccountEntity) {
        viewModelScope.launch {
            repository.updateAccount(account)
        }
    }

    fun deleteAccount(account: com.example.data.entity.AccountEntity) {
        viewModelScope.launch {
            repository.deleteAccount(account)
        }
    }

    fun addBudget(budget: com.example.data.entity.BudgetEntity) {
        viewModelScope.launch {
            repository.insertBudget(budget)
        }
    }

    fun updateBudget(budget: com.example.data.entity.BudgetEntity) {
        viewModelScope.launch {
            repository.updateBudget(budget)
        }
    }

    fun deleteBudget(budget: com.example.data.entity.BudgetEntity) {
        viewModelScope.launch {
            repository.deleteBudget(budget)
        }
    }

    fun addGoal(goal: com.example.data.entity.SavingsGoalEntity) {
        viewModelScope.launch {
            repository.insertGoal(goal)
        }
    }

    fun updateGoal(goal: com.example.data.entity.SavingsGoalEntity) {
        viewModelScope.launch {
            repository.updateGoal(goal)
        }
    }

    fun deleteGoal(goal: com.example.data.entity.SavingsGoalEntity) {
        viewModelScope.launch {
            repository.deleteGoal(goal)
        }
    }

    fun contributeToGoal(goal: com.example.data.entity.SavingsGoalEntity, amount: Double) {
        viewModelScope.launch {
            val updated = goal.copy(
                savedAmount = (goal.savedAmount + amount).coerceAtMost(goal.targetAmount),
                isCompleted = (goal.savedAmount + amount) >= goal.targetAmount
            )
            repository.updateGoal(updated)
        }
    }

    // Raw transactions
    val allTransactions: StateFlow<List<TransactionEntity>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentTransactions: StateFlow<List<TransactionEntity>> = repository.recentTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allDebts: StateFlow<List<DebtEntity>> = repository.allDebts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allFavorites: StateFlow<List<FavoriteEntity>> = repository.allFavorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allClosings: StateFlow<List<DailyClosingEntity>> = repository.allClosings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Balance visibility toggle
    private val _isBalanceVisible = MutableStateFlow(true)
    val isBalanceVisible: StateFlow<Boolean> = _isBalanceVisible.asStateFlow()

    fun toggleBalanceVisibility() {
        _isBalanceVisible.value = !_isBalanceVisible.value
    }

    // Available balance
    val availableBalance: StateFlow<Double> = allTransactions.combine(_isBalanceVisible) { list, _ ->
        var total = 0.0
        for (tx in list) {
            if (tx.isIncome) {
                total += tx.amount
            } else {
                total -= tx.amount
            }
        }
        total
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Today's summary
    val todaySummary: StateFlow<TodaySummary> = allTransactions.combine(_isBalanceVisible) { list, _ ->
        val now = Calendar.getInstance()
        var income = 0.0
        var expense = 0.0
        var inCount = 0
        var exCount = 0

        val cal = Calendar.getInstance()
        for (tx in list) {
            cal.timeInMillis = tx.timestamp
            val isToday = cal.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                    cal.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)

            if (isToday) {
                if (tx.isIncome) {
                    income += tx.amount
                    inCount++
                } else {
                    expense += tx.amount
                    exCount++
                }
            }
        }
        TodaySummary(
            income = income,
            expense = expense,
            balance = income - expense,
            incomeCount = inCount,
            expenseCount = exCount,
            totalCount = inCount + exCount
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TodaySummary())

    // Debts summary for Dashboard (Me Deben vs Debo)
    val totalMeDeben: StateFlow<Double> = allDebts.combine(_isBalanceVisible) { debts, _ ->
        debts.filter { !it.isIOwe }.sumOf { it.remainingAmount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val countMeDeben: StateFlow<Int> = allDebts.combine(_isBalanceVisible) { debts, _ ->
        debts.count { !it.isIOwe && it.remainingAmount > 0 }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalDebo: StateFlow<Double> = allDebts.combine(_isBalanceVisible) { debts, _ ->
        debts.filter { it.isIOwe }.sumOf { it.remainingAmount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val countDebo: StateFlow<Int> = allDebts.combine(_isBalanceVisible) { debts, _ ->
        debts.count { it.isIOwe && it.remainingAmount > 0 }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun getPaymentsForDebt(debtId: Long): Flow<List<DebtPaymentEntity>> {
        return repository.getPaymentsForDebt(debtId)
    }

    // Search and filter for Movimientos screen
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilterType = MutableStateFlow("Todos")
    val selectedFilterType: StateFlow<String> = _selectedFilterType.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilterType(type: String) {
        _selectedFilterType.value = type
    }

    val filteredTransactions: StateFlow<List<TransactionEntity>> = combine(
        allTransactions,
        _searchQuery,
        _selectedFilterType
    ) { list, query, filter ->
        list.filter { tx ->
            val matchesQuery = query.isBlank() ||
                    tx.concept.contains(query, ignoreCase = true) ||
                    tx.category.contains(query, ignoreCase = true) ||
                    tx.paymentMethod.contains(query, ignoreCase = true) ||
                    tx.note.contains(query, ignoreCase = true)

            val matchesFilter = when (filter) {
                "Ingresos" -> tx.isIncome
                "Gastos" -> !tx.isIncome
                "Deudas" -> tx.category.equals("Deudas", ignoreCase = true) ||
                        tx.type.contains("DEUDA") || tx.type.contains("PRESTAMO")
                "Transferencias" -> tx.type.contains("TRANSFERENCIA") ||
                        tx.paymentMethod.contains("Transferencia", ignoreCase = true)
                else -> true
            }

            matchesQuery && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Monthly Report & Comparison
    private val _selectedMonthOffset = MutableStateFlow(0) // 0 = Current month, 1 = Previous month, 2 = 2 months ago
    val selectedMonthOffset: StateFlow<Int> = _selectedMonthOffset.asStateFlow()

    fun setSelectedMonthOffset(offset: Int) {
        _selectedMonthOffset.value = offset
    }

    val monthlyReport: StateFlow<MonthlyReportData> = combine(
        allTransactions,
        _selectedMonthOffset,
        availableBalance
    ) { transactions, offset, currentBalance ->
        calculateMonthlyReport(transactions, offset, currentBalance)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MonthlyReportData("Este mes"))

    private fun calculateMonthlyReport(
        transactions: List<TransactionEntity>,
        offset: Int,
        currentBalance: Double
    ): MonthlyReportData {
        val targetCal = Calendar.getInstance().apply {
            add(Calendar.MONTH, -offset)
        }
        val targetYear = targetCal.get(Calendar.YEAR)
        val targetMonth = targetCal.get(Calendar.MONTH)

        val monthFormat = SimpleDateFormat("MMMM yyyy", Locale("es", "CO"))
        val monthName = monthFormat.format(targetCal.time).replaceFirstChar { it.uppercase() }

        val prevCal = Calendar.getInstance().apply {
            timeInMillis = targetCal.timeInMillis
            add(Calendar.MONTH, -1)
        }
        val prevYear = prevCal.get(Calendar.YEAR)
        val prevMonth = prevCal.get(Calendar.MONTH)

        val cal = Calendar.getInstance()
        val currentMonthTxs = mutableListOf<TransactionEntity>()
        val prevMonthTxs = mutableListOf<TransactionEntity>()

        for (tx in transactions) {
            cal.timeInMillis = tx.timestamp
            val y = cal.get(Calendar.YEAR)
            val m = cal.get(Calendar.MONTH)

            if (y == targetYear && m == targetMonth) {
                currentMonthTxs.add(tx)
            } else if (y == prevYear && m == prevMonth) {
                prevMonthTxs.add(tx)
            }
        }

        val totalIncome = currentMonthTxs.filter { it.isIncome }.sumOf { it.amount }
        val totalExpense = currentMonthTxs.filter { !it.isIncome }.sumOf { it.amount }
        val balance = totalIncome - totalExpense

        val prevIncome = prevMonthTxs.filter { it.isIncome }.sumOf { it.amount }
        val prevExpense = prevMonthTxs.filter { !it.isIncome }.sumOf { it.amount }

        val incomeChange: Double? = if (prevIncome > 0) {
            ((totalIncome - prevIncome) / prevIncome) * 100.0
        } else null

        val expenseChange: Double? = if (prevExpense > 0) {
            ((totalExpense - prevExpense) / prevExpense) * 100.0
        } else null

        val incomeByCat = currentMonthTxs
            .filter { it.isIncome }
            .groupBy { it.category }
            .mapValues { it.value.sumOf { tx -> tx.amount } }
            .toList()
            .sortedByDescending { it.second }

        val expenseByCat = currentMonthTxs
            .filter { !it.isIncome }
            .groupBy { it.category }
            .mapValues { it.value.sumOf { tx -> tx.amount } }
            .toList()
            .sortedByDescending { it.second }

        // Weekly breakdown: 4 weeks
        val weekMap = mutableMapOf<Int, Pair<Double, Double>>() // week -> (income, expense)
        for (i in 1..4) weekMap[i] = Pair(0.0, 0.0)

        for (tx in currentMonthTxs) {
            cal.timeInMillis = tx.timestamp
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val week = ((day - 1) / 7 + 1).coerceIn(1, 4)
            val current = weekMap[week] ?: Pair(0.0, 0.0)
            if (tx.isIncome) {
                weekMap[week] = Pair(current.first + tx.amount, current.second)
            } else {
                weekMap[week] = Pair(current.first, current.second + tx.amount)
            }
        }

        val weeklyList = (1..4).map { w ->
            val data = weekMap[w] ?: Pair(0.0, 0.0)
            WeeklyBarData(
                weekLabel = "Sem $w",
                income = data.first,
                expense = data.second
            )
        }

        val daysInMonth = targetCal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val dailyAvgExpense = if (daysInMonth > 0) totalExpense / daysInMonth else 0.0

        val incomeByDay = currentMonthTxs.filter { it.isIncome }
            .groupBy {
                cal.timeInMillis = it.timestamp
                cal.get(Calendar.DAY_OF_MONTH)
            }
            .mapValues { it.value.sumOf { tx -> tx.amount } }
        val highestIncome = incomeByDay.maxByOrNull { it.value }
        val highestIncomeDay = if (highestIncome != null && highestIncome.value > 0) {
            "Día ${highestIncome.key} ($ ${highestIncome.value.toInt()})"
        } else "-"

        val expenseByDay = currentMonthTxs.filter { !it.isIncome }
            .groupBy {
                cal.timeInMillis = it.timestamp
                cal.get(Calendar.DAY_OF_MONTH)
            }
            .mapValues { it.value.sumOf { tx -> tx.amount } }
        val highestExpense = expenseByDay.maxByOrNull { it.value }
        val highestExpenseDay = if (highestExpense != null && highestExpense.value > 0) {
            "Día ${highestExpense.key} ($ ${highestExpense.value.toInt()})"
        } else "-"

        return MonthlyReportData(
            monthName = monthName,
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            balance = balance,
            currentCash = currentBalance,
            dailyAverageExpense = dailyAvgExpense,
            highestIncomeDay = highestIncomeDay,
            highestExpenseDay = highestExpenseDay,
            incomeByCategory = incomeByCat,
            expenseByCategory = expenseByCat,
            weeklyBreakdown = weeklyList,
            prevMonthIncome = prevIncome,
            prevMonthExpense = prevExpense,
            incomeChangePercent = incomeChange,
            expenseChangePercent = expenseChange
        )
    }

    // CRUD operations
    fun addTransaction(tx: TransactionEntity) {
        viewModelScope.launch {
            repository.insertTransaction(tx)
            notifyWidgetUpdate()
        }
    }

    fun deleteTransaction(tx: TransactionEntity) {
        viewModelScope.launch {
            repository.deleteTransaction(tx)
            notifyWidgetUpdate()
        }
    }

    fun addDebt(debt: DebtEntity, registerCashMovement: Boolean = false) {
        viewModelScope.launch {
            repository.insertDebt(debt, registerCashMovement)
            notifyWidgetUpdate()
        }
    }

    fun deleteDebt(debt: DebtEntity) {
        viewModelScope.launch {
            repository.deleteDebt(debt)
            notifyWidgetUpdate()
        }
    }

    fun recordDebtPayment(debtId: Long, amount: Double, method: String, note: String) {
        viewModelScope.launch {
            repository.recordDebtPayment(debtId, amount, method, note)
            notifyWidgetUpdate()
        }
    }

    // Favorites operations
    fun addFavorite(favorite: FavoriteEntity) {
        viewModelScope.launch {
            repository.insertFavorite(favorite)
        }
    }

    fun deleteFavorite(favorite: FavoriteEntity) {
        viewModelScope.launch {
            repository.deleteFavorite(favorite)
        }
    }

    // Daily Closing
    fun closeDay(
        expectedCash: Double,
        actualCash: Double,
        note: String,
        createCashAdjustment: Boolean
    ) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateKey = sdf.format(now)

            val summary = todaySummary.value
            val diff = actualCash - expectedCash
            val status = when {
                kotlin.math.abs(diff) < 0.01 -> "CUADRADA"
                diff < 0 -> "FALTANTE"
                else -> "SOBRANTE"
            }

            val closing = DailyClosingEntity(
                dateKey = dateKey,
                timestamp = now,
                totalIncome = summary.income,
                totalExpense = summary.expense,
                balance = summary.balance,
                expectedCash = expectedCash,
                actualCash = actualCash,
                difference = diff,
                incomeCount = summary.incomeCount,
                expenseCount = summary.expenseCount,
                totalMovements = summary.totalCount,
                status = status,
                note = note.trim()
            )
            repository.insertClosing(closing)

            // If user wants to create an explicit cash adjustment for the difference:
            if (createCashAdjustment && kotlin.math.abs(diff) >= 0.01) {
                val isPositiveDiff = diff > 0
                val adjustmentTx = TransactionEntity(
                    type = AccountingType.AJUSTE_CAJA.name,
                    concept = if (isPositiveDiff) "Ajuste de caja (sobrante)" else "Ajuste de caja (faltante)",
                    category = "Ajuste",
                    paymentMethod = "Efectivo",
                    amount = kotlin.math.abs(diff),
                    isIncome = isPositiveDiff,
                    timestamp = now,
                    note = "Generado desde Cierre Diario: $note"
                )
                repository.insertTransaction(adjustmentTx)
            }
            notifyWidgetUpdate()
        }
    }

    fun deleteClosing(closing: DailyClosingEntity) {
        viewModelScope.launch {
            repository.deleteClosing(closing)
            notifyWidgetUpdate()
        }
    }

    fun resetDemoData() {
        viewModelScope.launch {
            repository.resetAllData()
            notifyWidgetUpdate()
        }
    }

    fun clearDatabaseForTesting() {
        viewModelScope.launch {
            repository.clearDatabaseForRealTesting()
            notifyWidgetUpdate()
        }
    }

    fun loadSampleData() {
        viewModelScope.launch {
            repository.seedDemoData()
            notifyWidgetUpdate()
        }
    }

    fun notifyWidgetUpdate() {
        try {
            val context = getApplication<Application>()
            com.example.widget.WidgetHelper.updateAllWidgets(context)
        } catch (_: Exception) {
            // Widget not yet initialized
        }
    }
}
