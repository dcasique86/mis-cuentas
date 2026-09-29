package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.entity.AccountingType
import com.example.data.entity.AccountEntity
import com.example.data.entity.BudgetEntity
import com.example.data.entity.SavingsGoalEntity
import com.example.data.entity.TransactionEntity
import com.example.ui.components.AddTransactionSheet
import com.example.ui.components.Formatters
import com.example.ui.components.TransactionSheetMode
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueLight
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PetrolDarkest
import com.example.ui.theme.PetrolLight
import com.example.ui.theme.PoppinsFontFamily
import com.example.ui.theme.SurfaceDark2
import com.example.ui.theme.TitaniumTextPrimary
import com.example.ui.theme.TitaniumTextSecondary
import com.example.ui.theme.TitaniumTextTertiary
import com.example.ui.components.VoiceInputDialog
import com.example.ui.screens.AccountsScreen
import com.example.ui.screens.AutoRulesScreen
import com.example.ui.screens.BudgetsScreen
import com.example.ui.screens.DailyClosingScreen
import com.example.ui.screens.DebtsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MoreScreen
import com.example.ui.screens.RecurringScreen
import com.example.ui.screens.ReportsScreen
import com.example.ui.screens.SavingsGoalsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TransactionsScreen
import com.example.ui.theme.Cream
import com.example.ui.theme.DeepGreen
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueLight
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PoppinsFontFamily
import com.example.ui.theme.SoftGreenLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TitaniumDivider
import com.example.ui.theme.TitaniumOrange
import com.example.ui.theme.TitaniumTextPrimary
import com.example.ui.theme.TitaniumTextSecondary
import com.example.ui.viewmodel.FinanceViewModel
import kotlinx.coroutines.launch

enum class AppNavTab(val title: String, val icon: ImageVector, val tag: String) {
    INICIO("Inicio", Icons.Default.Home, "tab_inicio"),
    MOVIMIENTOS("Movimientos", Icons.Default.ReceiptLong, "tab_movimientos"),
    DEUDAS("Deudas", Icons.Default.CreditCard, "tab_deudas"),
    METAS("Metas", Icons.Default.Savings, "tab_metas"),
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
    var showSettingsScreen by remember { mutableStateOf(false) }
    var showAccountsScreen by remember { mutableStateOf(false) }
    var showBudgetsScreen by remember { mutableStateOf(false) }
    var showGoalsScreen by remember { mutableStateOf(false) }
    var showRecurringScreen by remember { mutableStateOf(false) }
    var showAutoRulesScreen by remember { mutableStateOf(false) }
    var showReportsScreen by remember { mutableStateOf(false) }
    var showMoreMenu by remember { mutableStateOf(false) }
    var showAdjustBalanceDialog by remember { mutableStateOf(false) }
    var showVoiceDialog by remember { mutableStateOf(false) }
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
                showMoreMenu = true
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
    val allAccounts by viewModel.allAccounts.collectAsState()
    val allBudgets by viewModel.allBudgets.collectAsState()
    val allGoals by viewModel.allGoals.collectAsState()
    val allRecurring by viewModel.allRecurring.collectAsState()
    val allRules by viewModel.allRules.collectAsState()

    val isFullScreen = showDailyClosingScreen || showSettingsScreen || showAccountsScreen ||
            showBudgetsScreen || showGoalsScreen || showRecurringScreen || showAutoRulesScreen || showReportsScreen

    val navigateToHome: () -> Unit = {
        showDailyClosingScreen = false
        showSettingsScreen = false
        showAccountsScreen = false
        showBudgetsScreen = false
        showGoalsScreen = false
        showRecurringScreen = false
        showAutoRulesScreen = false
        showReportsScreen = false
        showMoreMenu = false
        currentTab = AppNavTab.INICIO
    }

    // Intercept back navigation so that the app never closes unexpectedly from configuration, sub-screens, or tabs
    BackHandler(enabled = showVoiceDialog) {
        showVoiceDialog = false
    }

    BackHandler(enabled = showMoreMenu) {
        showMoreMenu = false
    }

    BackHandler(enabled = isFullScreen) {
        navigateToHome()
    }

    BackHandler(enabled = !isFullScreen && !showMoreMenu && currentTab != AppNavTab.INICIO) {
        currentTab = AppNavTab.INICIO
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Cream,
        bottomBar = {
            if (!isFullScreen) {
                NavigationBar(
                    containerColor = com.example.ui.theme.PetrolDarkest,
                    tonalElevation = 8.dp
                ) {
                    AppNavTab.values().forEach { tab ->
                        if (tab == AppNavTab.MAS) {
                            NavigationBarItem(
                                selected = showMoreMenu,
                                onClick = { showMoreMenu = !showMoreMenu },
                                icon = {
                                    Box {
                                        Icon(
                                            imageVector = tab.icon,
                                            contentDescription = tab.title,
                                            modifier = Modifier.size(24.dp)
                                        )

                                        DropdownMenu(
                                            expanded = showMoreMenu,
                                            onDismissRequest = { showMoreMenu = false },
                                            modifier = Modifier
                                                .width(260.dp)
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(com.example.ui.theme.SurfaceDark1)
                                                .border(1.dp, com.example.ui.theme.PetrolBorder, RoundedCornerShape(16.dp))
                                                .testTag("more_dropdown_menu")
                                        ) {
                                            Text(
                                                text = "Otras funciones",
                                                fontFamily = PoppinsFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = com.example.ui.theme.GoldPrimary,
                                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                            )
                                            HorizontalDivider(color = com.example.ui.theme.SurfaceDarkDivider)

                                            DropdownMenuItem(
                                                text = {
                                                    Column {
                                                        Text("Reportes", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TitaniumTextPrimary)
                                                        Text("Gráficos y balance mensual", fontFamily = PoppinsFontFamily, fontSize = 10.5.sp, color = TitaniumTextSecondary)
                                                    }
                                                },
                                                leadingIcon = {
                                                    Icon(Icons.Default.BarChart, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(20.dp))
                                                },
                                                onClick = {
                                                    showMoreMenu = false
                                                    showReportsScreen = true
                                                },
                                                modifier = Modifier.testTag("menu_option_reports")
                                            )

                                            DropdownMenuItem(
                                                text = {
                                                    Column {
                                                        Text("Presupuestos", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TitaniumTextPrimary)
                                                        Text("Límites de gasto por rubro", fontFamily = PoppinsFontFamily, fontSize = 10.5.sp, color = TitaniumTextSecondary)
                                                    }
                                                },
                                                leadingIcon = {
                                                    Icon(Icons.Default.PieChart, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(20.dp))
                                                },
                                                onClick = {
                                                    showMoreMenu = false
                                                    showBudgetsScreen = true
                                                },
                                                modifier = Modifier.testTag("menu_option_budgets")
                                            )

                                            DropdownMenuItem(
                                                text = {
                                                    Column {
                                                        Text("Cuentas y Tarjetas", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TitaniumTextPrimary)
                                                        Text("Bancos, billeteras y efectivo", fontFamily = PoppinsFontFamily, fontSize = 10.5.sp, color = TitaniumTextSecondary)
                                                    }
                                                },
                                                leadingIcon = {
                                                    Icon(Icons.Default.AccountBalance, contentDescription = null, tint = Color(0xFF673AB7), modifier = Modifier.size(20.dp))
                                                },
                                                onClick = {
                                                    showMoreMenu = false
                                                    showAccountsScreen = true
                                                },
                                                modifier = Modifier.testTag("menu_option_accounts")
                                            )

                                            DropdownMenuItem(
                                                text = {
                                                    Column {
                                                        Text("Cierre del Día", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TitaniumTextPrimary)
                                                        Text("Arqueo y balance de caja", fontFamily = PoppinsFontFamily, fontSize = 10.5.sp, color = TitaniumTextSecondary)
                                                    }
                                                },
                                                leadingIcon = {
                                                    Icon(Icons.Default.Lock, contentDescription = null, tint = TitaniumOrange, modifier = Modifier.size(20.dp))
                                                },
                                                onClick = {
                                                    showMoreMenu = false
                                                    showDailyClosingScreen = true
                                                },
                                                modifier = Modifier.testTag("menu_option_daily_closing")
                                            )

                                            DropdownMenuItem(
                                                text = {
                                                    Column {
                                                        Text("Gastos Recurrentes", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TitaniumTextPrimary)
                                                        Text("Suscripciones y cobros fijos", fontFamily = PoppinsFontFamily, fontSize = 10.5.sp, color = TitaniumTextSecondary)
                                                    }
                                                },
                                                leadingIcon = {
                                                    Icon(Icons.Default.Repeat, contentDescription = null, tint = Color(0xFF009688), modifier = Modifier.size(20.dp))
                                                },
                                                onClick = {
                                                    showMoreMenu = false
                                                    showRecurringScreen = true
                                                },
                                                modifier = Modifier.testTag("menu_option_recurring")
                                            )

                                            DropdownMenuItem(
                                                text = {
                                                    Column {
                                                        Text("Reglas Inteligentes", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TitaniumTextPrimary)
                                                        Text("Categorización automática", fontFamily = PoppinsFontFamily, fontSize = 10.5.sp, color = TitaniumTextSecondary)
                                                    }
                                                },
                                                leadingIcon = {
                                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFFFF9800), modifier = Modifier.size(20.dp))
                                                },
                                                onClick = {
                                                    showMoreMenu = false
                                                    showAutoRulesScreen = true
                                                },
                                                modifier = Modifier.testTag("menu_option_auto_rules")
                                            )

                                            DropdownMenuItem(
                                                text = {
                                                    Column {
                                                        Text("Ajustar Saldo", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TitaniumTextPrimary)
                                                        Text("Cuadre rápido de efectivo", fontFamily = PoppinsFontFamily, fontSize = 10.5.sp, color = TitaniumTextSecondary)
                                                    }
                                                },
                                                leadingIcon = {
                                                    Icon(Icons.Default.Tune, contentDescription = null, tint = Color(0xFF455A64), modifier = Modifier.size(20.dp))
                                                },
                                                onClick = {
                                                    showMoreMenu = false
                                                    showAdjustBalanceDialog = true
                                                },
                                                modifier = Modifier.testTag("menu_option_adjust_balance")
                                            )

                                            HorizontalDivider(color = TitaniumDivider)

                                            DropdownMenuItem(
                                                text = {
                                                    Column {
                                                        Text("Configuración", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TitaniumTextPrimary)
                                                        Text("Ajustes generales y datos", fontFamily = PoppinsFontFamily, fontSize = 10.5.sp, color = TitaniumTextSecondary)
                                                    }
                                                },
                                                leadingIcon = {
                                                    Icon(Icons.Default.Settings, contentDescription = null, tint = TitaniumTextSecondary, modifier = Modifier.size(20.dp))
                                                },
                                                onClick = {
                                                    showMoreMenu = false
                                                    showSettingsScreen = true
                                                },
                                                modifier = Modifier.testTag("menu_option_settings")
                                            )
                                        }
                                    }
                                },
                                label = {
                                    Text(
                                        text = tab.title,
                                        fontFamily = PoppinsFontFamily,
                                        fontWeight = if (showMoreMenu) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 11.sp,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = ElectricBlue,
                                    selectedTextColor = ElectricBlue,
                                    indicatorColor = ElectricBlueLight,
                                    unselectedIconColor = TitaniumTextSecondary,
                                    unselectedTextColor = TitaniumTextSecondary
                                ),
                                modifier = Modifier.testTag(tab.tag)
                            )
                        } else {
                            val isSelected = currentTab == tab && !showMoreMenu
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = {
                                    showMoreMenu = false
                                    currentTab = tab
                                },
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
                                        fontSize = if (tab == AppNavTab.MOVIMIENTOS) 9.5.sp else 11.sp,
                                        letterSpacing = if (tab == AppNavTab.MOVIMIENTOS) (-0.4).sp else 0.sp,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = ElectricBlue,
                                    selectedTextColor = ElectricBlue,
                                    indicatorColor = ElectricBlueLight,
                                    unselectedIconColor = TitaniumTextSecondary,
                                    unselectedTextColor = TitaniumTextSecondary
                                ),
                                modifier = Modifier.testTag(tab.tag)
                            )
                        }
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
                onBackClick = { navigateToHome() },
                modifier = Modifier.padding(innerPadding)
            )
        } else if (showSettingsScreen) {
            SettingsScreen(
                currentBalance = availableBalance,
                accounts = allAccounts,
                onNavigateToAccounts = {
                    showSettingsScreen = false
                    showAccountsScreen = true
                },
                onAddAccount = { viewModel.addAccount(it) },
                onUpdateAccount = { viewModel.updateAccount(it) },
                onDeleteAccount = { viewModel.deleteAccount(it) },
                onBackClick = { navigateToHome() },
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
                onDailyClosingClick = {
                    showSettingsScreen = false
                    showDailyClosingScreen = true
                },
                modifier = Modifier.padding(innerPadding)
            )
        } else if (showAccountsScreen) {
            AccountsScreen(
                accounts = allAccounts,
                onAddAccount = { viewModel.addAccount(it) },
                onUpdateAccount = { viewModel.updateAccount(it) },
                onDeleteAccount = { viewModel.deleteAccount(it) },
                onBackClick = { navigateToHome() },
                modifier = Modifier.padding(innerPadding)
            )
        } else if (showBudgetsScreen) {
            BudgetsScreen(
                budgets = allBudgets,
                allTransactions = allTransactions,
                onAddBudget = { viewModel.addBudget(it) },
                onUpdateBudget = { viewModel.updateBudget(it) },
                onDeleteBudget = { viewModel.deleteBudget(it) },
                onBackClick = { navigateToHome() },
                modifier = Modifier.padding(innerPadding)
            )
        } else if (showGoalsScreen) {
            SavingsGoalsScreen(
                goals = allGoals,
                onAddGoal = { viewModel.addGoal(it) },
                onUpdateGoal = { viewModel.updateGoal(it) },
                onDeleteGoal = { viewModel.deleteGoal(it) },
                onContribute = { goal: SavingsGoalEntity, amount: Double -> viewModel.contributeToGoal(goal, amount) },
                onBackClick = { navigateToHome() },
                modifier = Modifier.padding(innerPadding)
            )
        } else if (showRecurringScreen) {
            RecurringScreen(
                recurringList = allRecurring,
                onAddRecurring = { viewModel.addRecurring(it) },
                onUpdateRecurring = { viewModel.updateRecurring(it) },
                onDeleteRecurring = { viewModel.deleteRecurring(it) },
                onApplyNow = { viewModel.applyRecurringNow(it) },
                onBackClick = { navigateToHome() },
                modifier = Modifier.padding(innerPadding)
            )
        } else if (showAutoRulesScreen) {
            AutoRulesScreen(
                rules = allRules,
                onAddRule = { viewModel.addRule(it) },
                onUpdateRule = { viewModel.updateRule(it) },
                onDeleteRule = { viewModel.deleteRule(it) },
                onBackClick = { navigateToHome() },
                modifier = Modifier.padding(innerPadding)
            )
        } else if (showReportsScreen) {
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
                onBackClick = { navigateToHome() },
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
                        accounts = allAccounts,
                        goals = allGoals,
                        onAddGoal = { viewModel.addGoal(it) },
                        onContributeToGoal = { goal, amount -> viewModel.contributeToGoal(goal, amount) },
                        totalMeDeben = totalMeDeben,
                        countMeDeben = countMeDeben,
                        totalDebo = totalDebo,
                        countDebo = countDebo,
                        onNavigateToDebts = { isIOwe ->
                            debtsInitialTabIsIOwe = isIOwe
                            currentTab = AppNavTab.DEUDAS
                        },
                        onDailyClosingClick = { showDailyClosingScreen = true },
                        onNavigateToAccounts = { showAccountsScreen = true },
                        onNavigateToBudgets = { showBudgetsScreen = true },
                        onNavigateToGoals = { currentTab = AppNavTab.METAS },
                        onNavigateToRecurring = { showRecurringScreen = true },
                        onNavigateToAutoRules = { showAutoRulesScreen = true },
                        onVoiceInputClick = { showVoiceDialog = true },
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
                        onMoreActionsClick = { showMoreMenu = true },
                        onViewAllTransactionsClick = { currentTab = AppNavTab.MOVIMIENTOS },
                        onSettingsClick = { showSettingsScreen = true },
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
                        onBackClick = { navigateToHome() },
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
                        onBackClick = { navigateToHome() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                AppNavTab.METAS -> {
                    SavingsGoalsScreen(
                        goals = allGoals,
                        onAddGoal = { viewModel.addGoal(it) },
                        onUpdateGoal = { viewModel.updateGoal(it) },
                        onDeleteGoal = { viewModel.deleteGoal(it) },
                        onContribute = { goal: SavingsGoalEntity, amount: Double -> viewModel.contributeToGoal(goal, amount) },
                        onBackClick = { navigateToHome() },
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
                        onNavigateToSettings = { showSettingsScreen = true },
                        onNavigateToAccounts = { showAccountsScreen = true },
                        onNavigateToBudgets = { showBudgetsScreen = true },
                        onNavigateToGoals = { currentTab = AppNavTab.METAS },
                        onNavigateToRecurring = { showRecurringScreen = true },
                        onNavigateToAutoRules = { showAutoRulesScreen = true },
                        onVoiceInputClick = { showVoiceDialog = true },
                        onBackClick = { navigateToHome() },
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
                rules = allRules,
                accounts = allAccounts,
                onSaveFavorite = { viewModel.addFavorite(it) },
                onDeleteFavorite = { viewModel.deleteFavorite(it) },
                onDismiss = {
                    coroutineScope.launch {
                        runCatching { sheetState.hide() }
                        showTransactionSheet = false
                    }
                },
                onSave = { newTx ->
                    viewModel.addTransaction(newTx)
                    coroutineScope.launch {
                        runCatching { sheetState.hide() }
                        showTransactionSheet = false
                    }
                }
            )
        }

        // Voice Input Dialog
        if (showVoiceDialog) {
            VoiceInputDialog(
                rules = allRules,
                onDismiss = { showVoiceDialog = false },
                onConfirm = { parsedTx, chosenMethod ->
                    viewModel.registerVoiceTransaction(parsedTx, chosenMethod)
                    showVoiceDialog = false
                }
            )
        }

        // Quick Adjust Balance Dialog
        if (showAdjustBalanceDialog) {
            var newBalanceText by remember { mutableStateOf(if (availableBalance > 0) availableBalance.toInt().toString() else "") }
            var adjustNote by remember { mutableStateOf("Ajuste manual de saldo") }

            AlertDialog(
                onDismissRequest = { showAdjustBalanceDialog = false },
                title = {
                    Text(
                        text = "Ajustar Saldo de Caja",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Saldo actual en el sistema: ${Formatters.formatMoney(availableBalance)}",
                            fontFamily = PoppinsFontFamily,
                            fontSize = 13.sp,
                            color = TitaniumTextSecondary
                        )
                        OutlinedTextField(
                            value = newBalanceText,
                            onValueChange = { input -> newBalanceText = Formatters.formatAmountInput(input) },
                            label = { Text("Nuevo Saldo Real ($)", color = Color(0xFFE2E8F0), fontWeight = FontWeight.SemiBold) },
                            prefix = { Text("$ ", color = GoldLight, fontWeight = FontWeight.Bold) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            textStyle = TextStyle(
                                fontFamily = PoppinsFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = PetrolDarkest,
                                unfocusedContainerColor = PetrolDarkest,
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = PetrolLight,
                                focusedLabelColor = GoldLight,
                                unfocusedLabelColor = Color(0xFFE2E8F0),
                                cursorColor = GoldPrimary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = adjustNote,
                            onValueChange = { adjustNote = it },
                            label = { Text("Nota / Motivo", color = Color(0xFFE2E8F0), fontWeight = FontWeight.SemiBold) },
                            singleLine = true,
                            textStyle = TextStyle(
                                fontFamily = PoppinsFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = PetrolDarkest,
                                unfocusedContainerColor = PetrolDarkest,
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = PetrolLight,
                                focusedLabelColor = GoldLight,
                                unfocusedLabelColor = Color(0xFFE2E8F0),
                                cursorColor = GoldPrimary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val newBal = if (newBalanceText.isNotBlank()) Formatters.parseAmountInput(newBalanceText) else availableBalance
                            val diff = newBal - availableBalance
                            val isIncome = diff >= 0
                            val tx = TransactionEntity(
                                type = AccountingType.AJUSTE_CAJA.name,
                                concept = "Ajuste de saldo",
                                category = "Ajuste",
                                paymentMethod = "Efectivo",
                                amount = kotlin.math.abs(diff),
                                isIncome = isIncome,
                                timestamp = System.currentTimeMillis(),
                                note = adjustNote.ifBlank { "Ajuste manual de saldo" }
                            )
                            viewModel.addTransaction(tx)
                            showAdjustBalanceDialog = false
                        }
                    ) {
                        Text("Guardar Ajuste")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showAdjustBalanceDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
