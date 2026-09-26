package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.entity.Periodicity
import com.example.data.entity.RecurringTransactionEntity
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
import com.example.ui.theme.TitaniumOrange
import com.example.ui.theme.TitaniumOrangeLight
import com.example.ui.theme.TitaniumTextPrimary
import com.example.ui.theme.TitaniumTextSecondary
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurringScreen(
    recurringList: List<RecurringTransactionEntity>,
    onAddRecurring: (RecurringTransactionEntity) -> Unit,
    onUpdateRecurring: (RecurringTransactionEntity) -> Unit,
    onDeleteRecurring: (RecurringTransactionEntity) -> Unit,
    onApplyNow: (RecurringTransactionEntity) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    var recurringToEdit by remember { mutableStateOf<RecurringTransactionEntity?>(null) }
    var recurringToDelete by remember { mutableStateOf<RecurringTransactionEntity?>(null) }

    // Summary calculation
    val activeList = recurringList.filter { it.isActive }
    val monthlyTotalExpenses = activeList.filter { !it.isIncome }.sumOf { item ->
        when (item.periodicity.uppercase()) {
            Periodicity.DAILY.name -> item.amount * 30.0
            Periodicity.WEEKLY.name -> item.amount * 4.33
            Periodicity.BIWEEKLY.name -> item.amount * 2.0
            Periodicity.MONTHLY.name -> item.amount
            Periodicity.YEARLY.name -> item.amount / 12.0
            else -> item.amount
        }
    }
    val monthlyTotalIncome = activeList.filter { it.isIncome }.sumOf { item ->
        when (item.periodicity.uppercase()) {
            Periodicity.DAILY.name -> item.amount * 30.0
            Periodicity.WEEKLY.name -> item.amount * 4.33
            Periodicity.BIWEEKLY.name -> item.amount * 2.0
            Periodicity.MONTHLY.name -> item.amount
            Periodicity.YEARLY.name -> item.amount / 12.0
            else -> item.amount
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = TitaniumLightBg,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "SUSCRIPCIONES",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 0.8.sp,
                            color = TitaniumTextSecondary
                        )
                        Text(
                            text = "Recurrentes y Fijos",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = TitaniumTextPrimary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("recurring_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = TitaniumTextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TitaniumLightBg)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    recurringToEdit = null
                    showDialog = true
                },
                containerColor = ElectricBlue,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("add_recurring_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva recurrente")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Cupertino Titanium Dark Card Summary
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = TitaniumDarkCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(ElectricBlueLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Repeat,
                                    contentDescription = null,
                                    tint = ElectricBlue,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "PROYECCIÓN MENSUAL",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    letterSpacing = 0.8.sp,
                                    color = Color(0xFF8E8E93)
                                )
                                Text(
                                    text = "${activeList.size} pagos fijos activos",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Gastos / Suscripciones",
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = 12.sp,
                                    color = Color(0xFFAEAEB2)
                                )
                                Text(
                                    text = "−${Formatters.formatMoney(monthlyTotalExpenses)}",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = CoralRed
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Ingresos fijos",
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = 12.sp,
                                    color = Color(0xFFAEAEB2)
                                )
                                Text(
                                    text = "+${Formatters.formatMoney(monthlyTotalIncome)}",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = EmeraldGreen
                                )
                            }
                        }
                    }
                }
            }

            if (recurringList.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(ElectricBlueLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Repeat,
                                    contentDescription = null,
                                    tint = ElectricBlue,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Sin transacciones recurrentes",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = TitaniumTextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Agrega suscripciones como Netflix, arriendo, servicios o salario fijo.",
                                fontFamily = PoppinsFontFamily,
                                fontSize = 12.sp,
                                color = TitaniumTextSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(recurringList, key = { it.id }) { item ->
                    RecurringItemCard(
                        recurring = item,
                        onApplyNow = { onApplyNow(item) },
                        onToggleActive = { onUpdateRecurring(item.copy(isActive = !item.isActive)) },
                        onEdit = {
                            recurringToEdit = item
                            showDialog = true
                        },
                        onDelete = { recurringToDelete = item }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    // Add / Edit Dialog
    if (showDialog) {
        RecurringDialog(
            initial = recurringToEdit,
            onDismiss = {
                showDialog = false
                recurringToEdit = null
            },
            onSave = { entity ->
                if (recurringToEdit != null) {
                    onUpdateRecurring(entity)
                } else {
                    onAddRecurring(entity)
                }
                showDialog = false
                recurringToEdit = null
            }
        )
    }

    // Delete Confirmation
    if (recurringToDelete != null) {
        val target = recurringToDelete!!
        AlertDialog(
            onDismissRequest = { recurringToDelete = null },
            containerColor = SurfaceWhite,
            title = {
                Text(
                    text = "Eliminar recurrente",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = CoralRed
                )
            },
            text = {
                Text(
                    text = "¿Deseas eliminar '${target.description}'? Las transacciones ya registradas en tu caja se conservarán.",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 14.sp,
                    color = TitaniumTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteRecurring(target)
                        recurringToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoralRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Eliminar", color = Color.White, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { recurringToDelete = null }) {
                    Text("Cancelar", color = TitaniumTextSecondary, fontFamily = PoppinsFontFamily)
                }
            }
        )
    }
}

@Composable
fun RecurringItemCard(
    recurring: RecurringTransactionEntity,
    onApplyNow: () -> Unit,
    onToggleActive: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    val now = System.currentTimeMillis()
    val daysUntilDue = TimeUnit.MILLISECONDS.toDays(recurring.nextDueDate - now)
    val sdf = remember { SimpleDateFormat("dd MMM", Locale("es", "CO")) }
    val dueDateLabel = sdf.format(recurring.nextDueDate)

    val dueBadgeText = when {
        !recurring.isActive -> "Pausada"
        daysUntilDue < 0 -> "Venció hace ${-daysUntilDue}d"
        daysUntilDue == 0L -> "¡Vence hoy!"
        daysUntilDue == 1L -> "Vence mañana"
        else -> "En $daysUntilDue días ($dueDateLabel)"
    }

    val dueBadgeColor = when {
        !recurring.isActive -> TitaniumTextSecondary
        daysUntilDue <= 0 -> CoralRed
        daysUntilDue <= 3 -> TitaniumOrange
        else -> EmeraldGreen
    }

    val dueBadgeBg = when {
        !recurring.isActive -> Color(0xFFF2F4F7)
        daysUntilDue <= 0 -> CoralRedLight
        daysUntilDue <= 3 -> TitaniumOrangeLight
        else -> EmeraldGreenLight
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                if (recurring.isIncome) EmeraldGreenLight else CoralRedLight
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (recurring.isIncome) "💰" else "💳",
                            fontSize = 20.sp
                        )
                    }
                    Column {
                        Text(
                            text = recurring.description,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = if (recurring.isActive) TitaniumTextPrimary else TitaniumTextSecondary
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = recurring.category,
                                fontFamily = PoppinsFontFamily,
                                fontSize = 12.sp,
                                color = TitaniumTextSecondary
                            )
                            Text(text = "•", fontSize = 12.sp, color = TitaniumTextSecondary)
                            Text(
                                text = recurring.periodicity.lowercase().replaceFirstChar { it.uppercase() },
                                fontFamily = PoppinsFontFamily,
                                fontSize = 12.sp,
                                color = TitaniumTextSecondary
                            )
                        }
                    }
                }

                // Amount
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = (if (recurring.isIncome) "+ " else "− ") + Formatters.formatMoney(recurring.amount),
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = if (!recurring.isActive) TitaniumTextSecondary else if (recurring.isIncome) EmeraldGreen else CoralRed
                    )
                    Box {
                        IconButton(onClick = { showMenu = true }, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Opciones", tint = TitaniumTextSecondary)
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(if (recurring.isActive) "Pausar" else "Activar", fontFamily = PoppinsFontFamily) },
                                onClick = {
                                    showMenu = false
                                    onToggleActive()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Editar", fontFamily = PoppinsFontFamily) },
                                onClick = {
                                    showMenu = false
                                    onEdit()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Eliminar", color = CoralRed, fontFamily = PoppinsFontFamily) },
                                onClick = {
                                    showMenu = false
                                    onDelete()
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = TitaniumDivider, thickness = 0.8.dp)
            Spacer(modifier = Modifier.height(10.dp))

            // Due badge & Apply button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = dueBadgeBg
                ) {
                    Text(
                        text = dueBadgeText,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp,
                        color = dueBadgeColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                if (recurring.isActive) {
                    Button(
                        onClick = onApplyNow,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (recurring.isIncome) EmeraldGreen else ElectricBlue,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (recurring.isIncome) "Cobrar ahora" else "Pagar ahora",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecurringDialog(
    initial: RecurringTransactionEntity?,
    onDismiss: () -> Unit,
    onSave: (RecurringTransactionEntity) -> Unit
) {
    var description by remember { mutableStateOf(initial?.description ?: "") }
    var amountText by remember { mutableStateOf(initial?.amount?.toInt()?.toString() ?: "") }
    var isIncome by remember { mutableStateOf(initial?.isIncome ?: false) }
    var category by remember { mutableStateOf(initial?.category ?: "Entretenimiento") }
    var periodicity by remember { mutableStateOf(initial?.periodicity ?: Periodicity.MONTHLY.name) }
    var paymentMethod by remember { mutableStateOf(initial?.paymentMethod ?: "Efectivo") }

    val categories = if (isIncome) {
        listOf("Trabajo", "Venta", "Honorarios", "Otro")
    } else {
        listOf("Entretenimiento", "Hogar", "Servicios", "Comida", "Transporte", "Salud", "Educación", "Otro")
    }

    val periodicities = listOf(
        Periodicity.DAILY,
        Periodicity.WEEKLY,
        Periodicity.BIWEEKLY,
        Periodicity.MONTHLY,
        Periodicity.YEARLY
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceWhite,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = if (initial == null) "Nueva recurrente" else "Editar recurrente",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                color = TitaniumTextPrimary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Income vs Expense Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            isIncome = false
                            category = "Entretenimiento"
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!isIncome) CoralRed else TitaniumLightBg,
                            contentColor = if (!isIncome) Color.White else TitaniumTextPrimary
                        ),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Gasto / Suscripción", fontSize = 11.sp, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            isIncome = true
                            category = "Trabajo"
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isIncome) EmeraldGreen else TitaniumLightBg,
                            contentColor = if (isIncome) Color.White else TitaniumTextPrimary
                        ),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Ingreso fijo", fontSize = 11.sp, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción (ej. Netflix, Alquiler)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricBlue,
                        focusedLabelColor = ElectricBlue,
                        unfocusedBorderColor = TitaniumBorder
                    )
                )

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Monto ($)") },
                    prefix = { Text("$ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricBlue,
                        focusedLabelColor = ElectricBlue,
                        unfocusedBorderColor = TitaniumBorder
                    )
                )

                // Periodicity selector chips
                Text(
                    text = "Frecuencia",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = TitaniumTextSecondary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    periodicities.forEach { p ->
                        val selected = periodicity == p.name
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (selected) ElectricBlue else TitaniumLightBg,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { periodicity = p.name }
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(vertical = 7.dp)
                            ) {
                                Text(
                                    text = p.displayName.take(4),
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 11.sp,
                                    color = if (selected) Color.White else TitaniumTextPrimary
                                )
                            }
                        }
                    }
                }

                // Category selector chips
                Text(
                    text = "Categoría",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = TitaniumTextSecondary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.take(4).forEach { cat ->
                        val selected = category == cat
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (selected) ElectricBlue else TitaniumLightBg,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { category = cat }
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(vertical = 7.dp)
                            ) {
                                Text(
                                    text = cat,
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 11.sp,
                                    color = if (selected) Color.White else TitaniumTextPrimary
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull() ?: 0.0
                    if (description.isNotBlank() && amount > 0) {
                        val nextDue = initial?.nextDueDate ?: (System.currentTimeMillis() + 30L * 24L * 3600L * 1000L)
                        onSave(
                            RecurringTransactionEntity(
                                id = initial?.id ?: 0,
                                description = description.trim(),
                                amount = amount,
                                isIncome = isIncome,
                                category = category,
                                periodicity = periodicity,
                                nextDueDate = nextDue,
                                paymentMethod = paymentMethod,
                                isActive = initial?.isActive ?: true
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ElectricBlue,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = description.isNotBlank() && (amountText.toDoubleOrNull() ?: 0.0) > 0
            ) {
                Text("Guardar", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TitaniumTextSecondary, fontFamily = PoppinsFontFamily)
            }
        }
    )
}
