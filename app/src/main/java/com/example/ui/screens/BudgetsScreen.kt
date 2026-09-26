package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.BudgetEntity
import com.example.data.entity.TransactionEntity
import com.example.ui.components.CategoryIcons
import com.example.ui.components.Formatters
import com.example.ui.theme.CoralRed
import com.example.ui.theme.CoralRedLight
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueLight
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.PoppinsFontFamily
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TitaniumBorder
import com.example.ui.theme.TitaniumDarkCard
import com.example.ui.theme.TitaniumDivider
import com.example.ui.theme.TitaniumLightBg
import com.example.ui.theme.TitaniumTextPrimary
import com.example.ui.theme.TitaniumTextSecondary
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetsScreen(
    budgets: List<BudgetEntity>,
    allTransactions: List<TransactionEntity>,
    onAddBudget: (BudgetEntity) -> Unit,
    onUpdateBudget: (BudgetEntity) -> Unit,
    onDeleteBudget: (BudgetEntity) -> Unit,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var budgetToEdit by remember { mutableStateOf<BudgetEntity?>(null) }
    var budgetToDelete by remember { mutableStateOf<BudgetEntity?>(null) }

    val currentMonthExpenses = remember(allTransactions) {
        val now = Calendar.getInstance()
        val currentMonth = now.get(Calendar.MONTH)
        val currentYear = now.get(Calendar.YEAR)
        val cal = Calendar.getInstance()

        val map = mutableMapOf<String, Double>()
        for (tx in allTransactions) {
            if (!tx.isIncome) {
                cal.timeInMillis = tx.timestamp
                if (cal.get(Calendar.MONTH) == currentMonth && cal.get(Calendar.YEAR) == currentYear) {
                    val key = tx.category.lowercase().trim()
                    map[key] = (map[key] ?: 0.0) + tx.amount
                }
            }
        }
        map
    }

    val totalBudget = budgets.sumOf { it.monthlyLimit }
    val totalSpent = budgets.sumOf { b ->
        currentMonthExpenses[b.category.lowercase().trim()] ?: 0.0
    }
    val overallPercentage = if (totalBudget > 0) (totalSpent / totalBudget).coerceIn(0.0, 1.5) else 0.0
    val overallPercentInt = (overallPercentage * 100).toInt()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = TitaniumLightBg,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "PRESUPUESTOS",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 0.8.sp,
                            color = TitaniumTextSecondary
                        )
                        Text(
                            text = "Control de Gastos",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = TitaniumTextPrimary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = TitaniumTextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showAddDialog = true },
                        modifier = Modifier.testTag("btn_add_budget_top")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Nuevo presupuesto",
                            tint = TitaniumTextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TitaniumLightBg)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = ElectricBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                elevation = androidx.compose.material3.FloatingActionButtonDefaults.elevation(3.dp),
                modifier = Modifier.testTag("fab_add_budget")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Nuevo Presupuesto",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Month Header & Hero Card (Titanium Dark Card)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = TitaniumDarkCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "PRESUPUESTO GLOBAL DEL MES",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 0.8.sp,
                                color = Color(0xFF8E8E93)
                            )
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (overallPercentage >= 1.0) Color(0xFF381E1E) else Color(0xFF1E382A)
                            ) {
                                Text(
                                    text = "$overallPercentInt% gastado",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = if (overallPercentage >= 1.0) CoralRed else EmeraldGreen,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = Formatters.formatMoney(totalSpent),
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                color = Color.White
                            )
                            Text(
                                text = " / ${Formatters.formatMoney(totalBudget)} COP",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = Color(0xFF8E8E93),
                                modifier = Modifier.padding(bottom = 3.dp, start = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        LinearProgressIndicator(
                            progress = { overallPercentage.toFloat().coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (overallPercentage >= 1.0) CoralRed
                            else if (overallPercentage >= 0.8) Color(0xFFFF9F0A)
                            else EmeraldGreen,
                            trackColor = Color(0xFF2C2C2E)
                        )
                    }
                }
            }

            // Categories label
            item {
                Text(
                    text = "Límites por categoría (${budgets.size})",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TitaniumTextPrimary
                )
            }

            if (budgets.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "📊", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No tienes presupuestos activos",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = TitaniumTextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Define límites mensuales para Comida, Transporte, Hogar...",
                                fontFamily = PoppinsFontFamily,
                                fontSize = 12.sp,
                                color = TitaniumTextSecondary
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = { showAddDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("+ Crear presupuesto", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                items(budgets, key = { it.id }) { b ->
                    val spent = currentMonthExpenses[b.category.lowercase().trim()] ?: 0.0
                    CupertinoBudgetItemRow(
                        budget = b,
                        spent = spent,
                        onEdit = { budgetToEdit = b },
                        onDelete = { budgetToDelete = b }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(90.dp))
            }
        }
    }

    if (showAddDialog) {
        CupertinoBudgetDialog(
            budget = null,
            onDismiss = { showAddDialog = false },
            onConfirm = {
                onAddBudget(it)
                showAddDialog = false
            }
        )
    }

    budgetToEdit?.let { b ->
        CupertinoBudgetDialog(
            budget = b,
            onDismiss = { budgetToEdit = null },
            onConfirm = {
                onUpdateBudget(it)
                budgetToEdit = null
            }
        )
    }

    budgetToDelete?.let { b ->
        AlertDialog(
            onDismissRequest = { budgetToDelete = null },
            containerColor = SurfaceWhite,
            title = { Text("Eliminar presupuesto", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, color = CoralRed) },
            text = { Text("¿Deseas eliminar el límite mensual para \"${b.category}\"?", fontFamily = PoppinsFontFamily, color = TitaniumTextSecondary) },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteBudget(b)
                        budgetToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoralRed)
                ) {
                    Text("Eliminar", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { budgetToDelete = null }) {
                    Text("Cancelar", fontFamily = PoppinsFontFamily)
                }
            }
        )
    }
}

@Composable
private fun CupertinoBudgetItemRow(
    budget: BudgetEntity,
    spent: Double,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    val fraction = if (budget.monthlyLimit > 0) (spent / budget.monthlyLimit).coerceIn(0.0, 1.5) else 0.0
    val percentage = (fraction * 100).toInt()

    val progressColor = when {
        fraction >= 1.0 -> CoralRed
        fraction >= 0.8 -> Color(0xFFFF9F0A)
        else -> EmeraldGreen
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(if (fraction >= 1.0) CoralRedLight else ElectricBlueLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = CategoryIcons.getIcon(budget.category),
                        contentDescription = null,
                        tint = if (fraction >= 1.0) CoralRed else ElectricBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = budget.category,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TitaniumTextPrimary
                    )
                    Text(
                        text = "${Formatters.formatMoney(spent)} / ${Formatters.formatMoney(budget.monthlyLimit)}",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 12.sp,
                        color = TitaniumTextSecondary
                    )
                }

                Text(
                    text = "$percentage%",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = progressColor
                )

                Box {
                    IconButton(onClick = { showMenu = true }, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Opciones", tint = TitaniumTextSecondary)
                    }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(
                            text = { Text("Editar", fontFamily = PoppinsFontFamily) },
                            onClick = {
                                showMenu = false
                                onEdit()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Eliminar", fontFamily = PoppinsFontFamily, color = CoralRed) },
                            onClick = {
                                showMenu = false
                                onDelete()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { fraction.toFloat().coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = progressColor,
                trackColor = TitaniumLightBg
            )

            if (fraction >= 1.0) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = CoralRed,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "¡Límite superado por ${Formatters.formatMoney(spent - budget.monthlyLimit)}!",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 11.sp,
                        color = CoralRed,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun CupertinoBudgetDialog(
    budget: BudgetEntity?,
    onDismiss: () -> Unit,
    onConfirm: (BudgetEntity) -> Unit
) {
    val categories = listOf("Comida", "Transporte", "Hogar", "Trabajo", "Tienda", "Entretenimiento", "Salud", "Educación", "Otros")
    var selectedCategory by remember { mutableStateOf(budget?.category ?: "Comida") }
    var limitText by remember { mutableStateOf(budget?.monthlyLimit?.toLong()?.toString() ?: "") }

    val isEdit = budget != null

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceWhite,
        title = {
            Text(
                text = if (isEdit) "Editar presupuesto" else "Nuevo presupuesto mensual",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TitaniumTextPrimary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Selecciona categoría:",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = TitaniumTextSecondary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.take(4).forEach { cat ->
                        val isSel = selectedCategory == cat
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { selectedCategory = cat },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSel) TitaniumDarkCard else TitaniumLightBg
                        ) {
                            Text(
                                text = cat,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 11.sp,
                                color = if (isSel) Color.White else TitaniumTextPrimary,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 2.dp),
                                maxLines = 1
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = limitText,
                    onValueChange = { input -> limitText = input.filter { it.isDigit() } },
                    label = { Text("Límite mensual ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricBlue,
                        unfocusedBorderColor = TitaniumBorder
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val limit = limitText.toDoubleOrNull() ?: 0.0
                    val entity = budget?.copy(
                        category = selectedCategory,
                        monthlyLimit = limit
                    ) ?: BudgetEntity(
                        category = selectedCategory,
                        monthlyLimit = limit,
                        monthYearKey = "DEFAULT"
                    )
                    onConfirm(entity)
                },
                enabled = limitText.isNotBlank() && (limitText.toDoubleOrNull() ?: 0.0) > 0,
                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Guardar", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", fontFamily = PoppinsFontFamily)
            }
        }
    )
}
