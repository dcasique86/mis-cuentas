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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import com.example.ui.components.Formatters
import com.example.ui.components.MisCuentasIcon
import com.example.ui.components.WidgetShowcaseDialog
import com.example.ui.theme.CoralRed
import com.example.ui.theme.CoralRedLight
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueLight
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PetrolDarkest
import com.example.ui.theme.PetrolLight
import com.example.ui.theme.PoppinsFontFamily
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TitaniumBorder
import com.example.ui.theme.TitaniumDarkCard
import com.example.ui.theme.TitaniumDivider
import com.example.ui.theme.TitaniumLightBg
import com.example.ui.theme.TitaniumTextPrimary
import com.example.ui.theme.TitaniumTextSecondary

@Composable
fun MoreScreen(
    currentBalance: Double,
    todayIncome: Double = 0.0,
    todayExpense: Double = 0.0,
    onAdjustCash: (newBalance: Double, note: String) -> Unit,
    onResetDemoData: () -> Unit,
    onClearDatabase: () -> Unit = {},
    onDailyClosingClick: () -> Unit = {},
    onAddIncomeClick: () -> Unit = {},
    onAddExpenseClick: () -> Unit = {},
    onTransferClick: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToAccounts: () -> Unit = {},
    onNavigateToBudgets: () -> Unit = {},
    onNavigateToGoals: () -> Unit = {},
    onNavigateToRecurring: () -> Unit = {},
    onNavigateToAutoRules: () -> Unit = {},
    onVoiceInputClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showAdjustDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showClearDbDialog by remember { mutableStateOf(false) }
    var showWidgetShowcase by remember { mutableStateOf(false) }
    var adjustAmountText by remember { mutableStateOf("") }
    var adjustNote by remember { mutableStateOf("Ajuste manual de caja") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TitaniumLightBg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(14.dp))

        // Screen Header with Back Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .background(SurfaceWhite, CircleShape)
                    .testTag("more_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver a Inicio",
                    tint = TitaniumTextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "MÁS OPCIONES",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 0.8.sp,
                    color = TitaniumTextSecondary
                )
                Text(
                    text = "Gestión y Ajustes",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = TitaniumTextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Brand Banner Card (Titanium Dark Card)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = TitaniumDarkCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MisCuentasIcon(size = 46.dp)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Mis Cuentas",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Text(
                            text = "“Tu dinero, en orden.”",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            color = ElectricBlue
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Aplicación personal para registrar tus movimientos en 2-5 segundos sin complicaciones financieras.",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 12.sp,
                    color = Color(0xFFAEAEB2),
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Acciones de gestión
        Text(
            text = "Herramientas y Finanzas",
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = TitaniumTextPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                OptionRow(
                    icon = Icons.Default.Mic,
                    iconTint = ElectricBlue,
                    iconBg = ElectricBlueLight,
                    title = "Entrada por voz",
                    subtitle = "Dicta 'Almuerzo 25 mil' o 'Taxi 30 mil' con auto-clasificación",
                    onClick = onVoiceInputClick
                )

                OptionRow(
                    icon = Icons.Default.Repeat,
                    iconTint = ElectricBlue,
                    iconBg = ElectricBlueLight,
                    title = "Transacciones recurrentes",
                    subtitle = "Suscripciones (Netflix, Spotify), servicios públicos y sueldos fijos",
                    onClick = onNavigateToRecurring
                )

                OptionRow(
                    icon = Icons.Default.AutoAwesome,
                    iconTint = ElectricBlue,
                    iconBg = ElectricBlueLight,
                    title = "Reglas automáticas",
                    subtitle = "Asigna categorías por palabras clave (ej. 'Uber' → Transporte)",
                    onClick = onNavigateToAutoRules
                )

                OptionRow(
                    icon = Icons.Default.Tune,
                    iconTint = ElectricBlue,
                    iconBg = ElectricBlueLight,
                    title = "Mis Cuentas (Billeteras y bancos)",
                    subtitle = "Efectivo, Nequi, Bancolombia, tarjetas de crédito y ahorros",
                    onClick = onNavigateToAccounts
                )

                OptionRow(
                    icon = Icons.Default.PieChart,
                    iconTint = ElectricBlue,
                    iconBg = ElectricBlueLight,
                    title = "Presupuestos mensuales",
                    subtitle = "Controla tus límites de gasto por categoría con alertas",
                    onClick = onNavigateToBudgets
                )

                OptionRow(
                    icon = Icons.Default.Settings,
                    iconTint = TitaniumTextPrimary,
                    iconBg = TitaniumLightBg,
                    title = "Configuración de la app",
                    subtitle = "Moneda, preferencias, recordatorios y ajustes generales",
                    onClick = onNavigateToSettings
                )

                OptionRow(
                    icon = Icons.Default.Widgets,
                    iconTint = ElectricBlue,
                    iconBg = ElectricBlueLight,
                    title = "Widgets de pantalla de inicio",
                    subtitle = "Diseños Compacto, Estándar y Extendido con acceso rápido",
                    onClick = { showWidgetShowcase = true }
                )

                OptionRow(
                    icon = Icons.Default.ReceiptLong,
                    iconTint = EmeraldGreen,
                    iconBg = EmeraldGreenLight,
                    title = "Cierre del día",
                    subtitle = "Cuenta tu efectivo y verifica tu caja diaria",
                    onClick = onDailyClosingClick
                )

                OptionRow(
                    icon = Icons.Default.Tune,
                    iconTint = TitaniumTextPrimary,
                    iconBg = TitaniumLightBg,
                    title = "Ajustar saldo de caja",
                    subtitle = "Cuadra tu saldo real actual (${Formatters.formatMoney(currentBalance)})",
                    onClick = {
                        adjustAmountText = currentBalance.toInt().toString()
                        showAdjustDialog = true
                    }
                )

                OptionRow(
                    icon = Icons.Default.DeleteSweep,
                    iconTint = CoralRed,
                    iconBg = CoralRedLight,
                    title = "Borrar base de datos (Reiniciar a $0)",
                    subtitle = "Empieza desde cero para pruebas reales de la aplicación",
                    onClick = { showClearDbDialog = true }
                )

                OptionRow(
                    icon = Icons.Default.Refresh,
                    iconTint = ElectricBlue,
                    iconBg = ElectricBlueLight,
                    title = "Cargar datos de ejemplo",
                    subtitle = "Carga ejemplos de ingresos, gastos y deudas",
                    onClick = { showResetDialog = true }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Principios de la app
        Text(
            text = "Filosofía de Mis Cuentas",
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = TitaniumTextPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                PillarRow(
                    icon = Icons.Default.Bolt,
                    title = "Rápido",
                    desc = "Registra tus movimientos en 2 a 5 segundos."
                )
                Spacer(modifier = Modifier.height(14.dp))
                PillarRow(
                    icon = Icons.Default.PieChart,
                    title = "Claro",
                    desc = "Visualiza tu saldo y números al instante."
                )
                Spacer(modifier = Modifier.height(14.dp))
                PillarRow(
                    icon = Icons.Default.Lock,
                    title = "Privado",
                    desc = "Tus datos viven solo en tu teléfono con base de datos local Room."
                )
                Spacer(modifier = Modifier.height(14.dp))
                PillarRow(
                    icon = Icons.Default.CheckCircle,
                    title = "Hecho para ti",
                    desc = "Simple, útil y sin complicaciones bancarias."
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }

    // Cash Adjustment Dialog
    if (showAdjustDialog) {
        AlertDialog(
            onDismissRequest = { showAdjustDialog = false },
            containerColor = SurfaceWhite,
            title = {
                Text(
                    text = "Ajuste de caja",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TitaniumTextPrimary
                )
            },
            text = {
                Column {
                    Text(
                        text = "Saldo registrado actual: ${Formatters.formatMoney(currentBalance)}",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 13.sp,
                        color = TitaniumTextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = adjustAmountText,
                        onValueChange = { input -> adjustAmountText = Formatters.formatAmountInput(input) },
                        label = { Text("Nuevo saldo real ($)", color = Color(0xFFE2E8F0), fontWeight = FontWeight.SemiBold) },
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
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = adjustNote,
                        onValueChange = { adjustNote = it },
                        label = { Text("Motivo del ajuste", color = Color(0xFFE2E8F0), fontWeight = FontWeight.SemiBold) },
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
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newBal = if (adjustAmountText.isNotBlank()) Formatters.parseAmountInput(adjustAmountText) else currentBalance
                        onAdjustCash(newBal, adjustNote)
                        showAdjustDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                ) {
                    Text("Aplicar ajuste", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAdjustDialog = false }) {
                    Text("Cancelar", fontFamily = PoppinsFontFamily)
                }
            }
        )
    }

    // Reset Demo Data Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            containerColor = SurfaceWhite,
            title = {
                Text(
                    text = "Cargar datos de ejemplo",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TitaniumTextPrimary
                )
            },
            text = {
                Text(
                    text = "¿Deseas cargar los datos de ejemplo iniciales (ventas, gastos comunes y deudas)? Esto restablecerá la base de datos a los datos de muestra.",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 13.sp,
                    color = TitaniumTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onResetDemoData()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                ) {
                    Text("Restablecer", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showResetDialog = false }) {
                    Text("Cancelar", fontFamily = PoppinsFontFamily)
                }
            }
        )
    }

    // Clear Database Dialog
    if (showClearDbDialog) {
        AlertDialog(
            onDismissRequest = { showClearDbDialog = false },
            containerColor = SurfaceWhite,
            title = {
                Text(
                    text = "¿Borrar toda la base de datos?",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = CoralRed
                )
            },
            text = {
                Text(
                    text = "Se eliminarán todos los movimientos, deudas, abonos, categorías y datos de prueba. La aplicación quedará en $0 lista para que registres tus datos reales. Esta acción no se puede deshacer.",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 13.sp,
                    color = TitaniumTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onClearDatabase()
                        showClearDbDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoralRed)
                ) {
                    Text("Sí, borrar todo", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showClearDbDialog = false }) {
                    Text("Cancelar", fontFamily = PoppinsFontFamily)
                }
            }
        )
    }

    // Widget showcase dialog
    if (showWidgetShowcase) {
        WidgetShowcaseDialog(
            balance = currentBalance,
            todayIncome = todayIncome,
            todayExpense = todayExpense,
            onDismiss = { showWidgetShowcase = false },
            onAddIncomeClick = {
                showWidgetShowcase = false
                onAddIncomeClick()
            },
            onAddExpenseClick = {
                showWidgetShowcase = false
                onAddExpenseClick()
            },
            onTransferClick = {
                showWidgetShowcase = false
                onTransferClick()
            }
        )
    }
}

@Composable
private fun OptionRow(
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = TitaniumTextPrimary
            )
            Text(
                text = subtitle,
                fontFamily = PoppinsFontFamily,
                fontSize = 12.sp,
                color = TitaniumTextSecondary
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = Color(0xFFC7C7CC),
            modifier = Modifier.size(13.dp)
        )
    }
}

@Composable
private fun PillarRow(
    icon: ImageVector,
    title: String,
    desc: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(ElectricBlueLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = TitaniumTextPrimary
            )
            Text(
                text = desc,
                fontFamily = PoppinsFontFamily,
                fontSize = 12.sp,
                color = TitaniumTextSecondary
            )
        }
    }
}
