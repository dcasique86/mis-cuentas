package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.entity.AccountingType
import com.example.data.entity.TransactionEntity
import com.example.ui.components.AddTransactionSheet
import com.example.ui.components.TransactionSheetMode
import com.example.ui.screens.DailyClosingScreen
import com.example.ui.screens.DebtsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MoreScreen
import com.example.ui.screens.ReportsScreen
import com.example.ui.screens.TransactionsScreen
import com.example.ui.theme.Cream
import com.example.ui.theme.DeepGreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PoppinsFontFamily
import com.example.ui.theme.SoftGreenLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.FinanceViewModel
import kotlinx.coroutines.launch

enum class AppNavTab(val title: String, val icon: ImageVector, val tag: String) {
    INICIO("Inicio", Icons.Default.Home, "tab_inicio"),
    MOVIMIENTOS("Movimientos", Icons.Default.ReceiptLong, "tab_movimientos"),
    DEUDAS("Deudas", Icons.Default.CreditCard, "tab_deudas"),
    REPORTES("Reportes", Icons.Default.BarChart, "tab_reportes"),
    MAS("Más", Icons.Default.MoreHoriz, "tab_mas")
}

class MainActivity : ComponentActivity() {
    companion object {
        const val EXTRA_QUICK_ACTION = "extra_quick_action"
        const val ACTION_ADD_INCOME = "action_add_income"
        const val ACTION_ADD_EXPENSE = "action_add_expense"
        const val ACTION_ADD_TRANSFER = "action_add_transfer"
        const val ACTION_NAVIGATE_MAS = "action_navigate_mas"
    }

    private val quickActionState = mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleIntent(intent)
        setContent {
            MyApplicationTheme {
                MisCuentasApp(
                    initialQuickAction = quickActionState.value,
                    onQuickActionHandled = { quickActionState.value = null }
                )
            }
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: android.content.Intent?) {
        val action = intent?.getStringExtra(EXTRA_QUICK_ACTION)
        if (action != null) {
            quickActionState.value = action
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisCuentasApp(
    viewModel: FinanceViewModel = viewModel(),
    initialQuickAction: String? = null,
    onQuickActionHandled: () -> Unit = {}
) {
    var currentTab by remember { mutableStateOf(AppNavTab.INICIO) }
    var showTransactionSheet by remember { mutableStateOf(false) }
    var transactionSheetMode by remember { mutableStateOf(TransactionSheetMode.EXPENSE) }
    var showDailyClosingScreen by remember { mutableStateOf(false) }
    var debtsInitialTabIsIOwe by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(initialQuickAction) {
        when (initialQuickAction) {
            MainActivity.ACTION_ADD_INCOME -> {
                currentTab = AppNavTab.INICIO
                transactionSheetMode = TransactionSheetMode.INCOME
                showTransactionSheet = true
                onQuickActionHandled()
            }
            MainActivity.ACTION_ADD_EXPENSE -> {
                currentTab = AppNavTab.INICIO
                transactionSheetMode = TransactionSheetMode.EXPENSE
                showTransactionSheet = true
                onQuickActionHandled()
            }
            MainActivity.ACTION_ADD_TRANSFER -> {
                currentTab = AppNavTab.INICIO
                transactionSheetMode = TransactionSheetMode.TRANSFER
                showTransactionSheet = true
                onQuickActionHandled()
            }
            MainActivity.ACTION_NAVIGATE_MAS -> {
                currentTab = AppNavTab.MAS
                onQuickActionHandled()
            }
        }
    }

    // Data from ViewModel
    val availableBalance by viewModel.availableBalance.collectAsState()
    val isBalanceVisible by viewModel.isBalanceVisible.collectAsState()
    val todaySummary by viewModel.todaySummary.collectAsState()
    val recentTransactions by viewModel.recentTransactions.collectAsState()
    val allTransactions by viewModel.allTransactions.collectAsState()
    val filteredTransactions by viewModel.filteredTransactions.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilterType by viewModel.selectedFilterType.collectAsState()
    val debts by viewModel.allDebts.collectAsState()
    val totalMeDeben by viewModel.totalMeDeben.collectAsState()
    val countMeDeben by viewModel.countMeDeben.collectAsState()
    val totalDebo by viewModel.totalDebo.collectAsState()
    val countDebo by viewModel.countDebo.collectAsState()
    val favorites by viewModel.allFavorites.collectAsState()
    val pastClosings by viewModel.allClosings.collectAsState()
    val monthlyReport by viewModel.monthlyReport.collectAsState()
    val selectedMonthOffset by viewModel.selectedMonthOffset.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Cream,
        bottomBar = {
            if (!showDailyClosingScreen) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 6.dp
                ) {
                    AppNavTab.values().forEach { tab ->
                        val isSelected = currentTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { currentTab = tab },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.title,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 11.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = DeepGreen,
                                selectedTextColor = DeepGreen,
                                indicatorColor = SoftGreenLight,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary
                            ),
                            modifier = Modifier.testTag(tab.tag)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        if (showDailyClosingScreen) {
            DailyClosingScreen(
                todaySummary = todaySummary,
                expectedCash = availableBalance,
                pastClosings = pastClosings,
                onSaveClosing = { actualCash, note ->
                    viewModel.closeDay(
                        expectedCash = availableBalance,
                        actualCash = actualCash,
                        note = note,
                        createCashAdjustment = true
                    )
                },
                onDeleteClosing = { viewModel.deleteClosing(it) },
                onBackClick = { showDailyClosingScreen = false },
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            when (currentTab) {
                AppNavTab.INICIO -> {
                    HomeScreen(
                        availableBalance = availableBalance,
                        isBalanceVisible = isBalanceVisible,
                        onToggleBalanceVisibility = { viewModel.toggleBalanceVisibility() },
                        todaySummary = todaySummary,
                        recentTransactions = recentTransactions,
                        totalMeDeben = totalMeDeben,
                        countMeDeben = countMeDeben,
                        totalDebo = totalDebo,
                        countDebo = countDebo,
                        onNavigateToDebts = { isIOwe ->
                            debtsInitialTabIsIOwe = isIOwe
                            currentTab = AppNavTab.DEUDAS
                        },
                        onDailyClosingClick = { showDailyClosingScreen = true },
                        onAddIncomeClick = {
                            transactionSheetMode = TransactionSheetMode.INCOME
                            showTransactionSheet = true
                        },
                        onAddExpenseClick = {
                            transactionSheetMode = TransactionSheetMode.EXPENSE
                            showTransactionSheet = true
                        },
                        onTransferClick = {
                            transactionSheetMode = TransactionSheetMode.TRANSFER
                            showTransactionSheet = true
                        },
                        onMoreActionsClick = { currentTab = AppNavTab.MAS },
                        onViewAllTransactionsClick = { currentTab = AppNavTab.MOVIMIENTOS },
                        onSettingsClick = { currentTab = AppNavTab.MAS },
                        onTransactionClick = { currentTab = AppNavTab.MOVIMIENTOS },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                AppNavTab.MOVIMIENTOS -> {
                    TransactionsScreen(
                        transactions = filteredTransactions,
                        searchQuery = searchQuery,
                        onSearchQueryChange = { viewModel.setSearchQuery(it) },
                        selectedFilter = selectedFilterType,
                        onFilterSelect = { viewModel.setFilterType(it) },
                        onDeleteTransaction = { viewModel.deleteTransaction(it) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                AppNavTab.DEUDAS -> {
                    DebtsScreen(
                        debts = debts,
                        initialTabIsIOwe = debtsInitialTabIsIOwe,
                        getPaymentsForDebt = { viewModel.getPaymentsForDebt(it) },
                        onAddDebt = { viewModel.addDebt(it) },
                        onPayDebt = { debtId, amount, method, note ->
                            viewModel.recordDebtPayment(debtId, amount, method, note)
                        },
                        onDeleteDebt = { viewModel.deleteDebt(it) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                AppNavTab.REPORTES -> {
                    ReportsScreen(
                        report = monthlyReport,
                        monthOffset = selectedMonthOffset,
                        onPreviousMonth = {
                            if (selectedMonthOffset > 0) {
                                viewModel.setSelectedMonthOffset(selectedMonthOffset - 1)
                            }
                        },
                        onNextMonth = {
                            viewModel.setSelectedMonthOffset(selectedMonthOffset + 1)
                        },
                        onResetCurrentMonth = {
                            viewModel.setSelectedMonthOffset(0)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                AppNavTab.MAS -> {
                    MoreScreen(
                        currentBalance = availableBalance,
                        todayIncome = todaySummary.income,
                        todayExpense = todaySummary.expense,
                        onAdjustCash = { newBalance, note ->
                            val diff = newBalance - availableBalance
                            val isIncome = diff >= 0
                            val tx = TransactionEntity(
                                type = AccountingType.AJUSTE_CAJA.name,
                                concept = "Ajuste de saldo",
                                category = "Ajuste",
                                paymentMethod = "Efectivo",
                                amount = kotlin.math.abs(diff),
                                isIncome = isIncome,
                                timestamp = System.currentTimeMillis(),
                                note = note
                            )
                            viewModel.addTransaction(tx)
                        },
                        onResetDemoData = { viewModel.loadSampleData() },
                        onClearDatabase = { viewModel.clearDatabaseForTesting() },
                        onDailyClosingClick = { showDailyClosingScreen = true },
                        onAddIncomeClick = {
                            transactionSheetMode = TransactionSheetMode.INCOME
                            showTransactionSheet = true
                        },
                        onAddExpenseClick = {
                            transactionSheetMode = TransactionSheetMode.EXPENSE
                            showTransactionSheet = true
                        },
                        onTransferClick = {
                            transactionSheetMode = TransactionSheetMode.TRANSFER
                            showTransactionSheet = true
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        // Fast Transaction Sheet
        if (showTransactionSheet) {
            AddTransactionSheet(
                initialMode = transactionSheetMode,
                sheetState = sheetState,
                favorites = favorites,
                onSaveFavorite = { viewModel.addFavorite(it) },
                onDeleteFavorite = { viewModel.deleteFavorite(it) },
                onDismiss = {
                    coroutineScope.launch {
                        sheetState.hide()
                        showTransactionSheet = false
                    }
                },
                onSave = { newTx ->
                    viewModel.addTransaction(newTx)
                    coroutineScope.launch {
                        sheetState.hide()
                        showTransactionSheet = false
                    }
                }
            )
        }
    }
}
