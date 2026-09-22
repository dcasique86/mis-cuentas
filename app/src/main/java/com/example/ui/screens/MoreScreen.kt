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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.data.entity.AccountingType
import com.example.data.entity.TransactionEntity
import com.example.ui.components.Formatters
import com.example.ui.components.MisCuentasIcon
import com.example.ui.theme.Coral
import com.example.ui.theme.Cream
import com.example.ui.theme.DeepGreen
import com.example.ui.theme.NeutralGray
import com.example.ui.theme.PoppinsFontFamily
import com.example.ui.theme.Sand
import com.example.ui.theme.SoftGreen
import com.example.ui.theme.SoftGreenLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material.icons.filled.DeleteSweep
import com.example.ui.components.WidgetShowcaseDialog

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
            .background(Cream)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Más opciones",
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Brand Banner Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                            color = TextPrimary
                        )
                        Text(
                            text = "“Tu dinero, en orden.”",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            color = DeepGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Aplicación personal para registrar tus movimientos en 2-5 segundos sin complicaciones financieras.",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Acciones de gestión
        Text(
            text = "Gestión de caja y datos",
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                // Widgets de inicio
                OptionRow(
                    icon = Icons.Default.Widgets,
                    title = "Widgets de pantalla de inicio",
                    subtitle = "Diseños Compacto, Estándar y Extendido con acceso rápido",
                    onClick = { showWidgetShowcase = true }
                )

                // Cierre del día
                OptionRow(
                    icon = Icons.Default.ReceiptLong,
                    title = "Cierre del día",
                    subtitle = "Cuenta tu efectivo y verifica tu caja diaria",
                    onClick = onDailyClosingClick
                )

                // Ajuste de caja
                OptionRow(
                    icon = Icons.Default.Tune,
                    title = "Ajustar saldo de caja",
                    subtitle = "Cuadra tu saldo real actual (${Formatters.formatMoney(currentBalance)})",
                    onClick = {
                        adjustAmountText = currentBalance.toInt().toString()
                        showAdjustDialog = true
                    }
                )

                // Borrar base de datos para pruebas reales
                OptionRow(
                    icon = Icons.Default.DeleteSweep,
                    title = "Borrar base de datos (Reiniciar a $0)",
                    subtitle = "Empieza desde cero para pruebas reales de la aplicación",
                    onClick = { showClearDbDialog = true }
                )

                // Restablecer datos de prueba
                OptionRow(
                    icon = Icons.Default.Refresh,
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
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                PillarRow(
                    icon = Icons.Default.Bolt,
                    title = "Rápido",
                    desc = "Registra tus movimientos en 2 a 5 segundos."
                )
                Spacer(modifier = Modifier.height(12.dp))
                PillarRow(
                    icon = Icons.Default.PieChart,
                    title = "Claro",
                    desc = "Visualiza tu saldo y números al instante."
                )
                Spacer(modifier = Modifier.height(12.dp))
                PillarRow(
                    icon = Icons.Default.Lock,
                    title = "Privado",
                    desc = "Tus datos viven solo en tu teléfono con base de datos local Room."
                )
                Spacer(modifier = Modifier.height(12.dp))
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
            containerColor = Cream,
            title = {
                Text(
                    text = "Ajuste de caja",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimary
                )
            },
            text = {
                Column {
                    Text(
                        text = "Ingresa el saldo real en efectivo que tienes ahora:",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = adjustAmountText,
                        onValueChange = { adjustAmountText = it.filter { c -> c.isDigit() } },
                        label = { Text("Nuevo saldo ($)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newAmount = adjustAmountText.toDoubleOrNull() ?: currentBalance
                        onAdjustCash(newAmount, adjustNote)
                        showAdjustDialog = false
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepGreen)
                ) {
                    Text("Aplicar Ajuste", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showAdjustDialog = false },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Cancelar", color = TextSecondary)
                }
            }
        )
    }

    // Clear Database Confirmation Dialog
    if (showClearDbDialog) {
        AlertDialog(
            onDismissRequest = { showClearDbDialog = false },
            containerColor = Cream,
            title = {
                Text(
                    text = "¿Borrar base de datos?",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimary
                )
            },
            text = {
                Text(
                    text = "Se eliminarán todas las transacciones, deudas y cierres registrados para que puedas probar la aplicación desde $0 con datos reales.",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onClearDatabase()
                        showClearDbDialog = false
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Coral)
                ) {
                    Text("Borrar todo", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showClearDbDialog = false },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Cancelar", color = TextSecondary)
                }
            }
        )
    }

    // Reset Confirmation Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            containerColor = Cream,
            title = {
                Text(
                    text = "¿Cargar datos de ejemplo?",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimary
                )
            },
            text = {
                Text(
                    text = "Se cargarán movimientos, deudas y favoritos de muestra para explorar las funciones de Mis Cuentas.",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onResetDemoData()
                        showResetDialog = false
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepGreen)
                ) {
                    Text("Cargar", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showResetDialog = false },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Cancelar", color = TextSecondary)
                }
            }
        )
    }

    // Widget Showcase Dialog
    if (showWidgetShowcase) {
        WidgetShowcaseDialog(
            balance = currentBalance,
            todayIncome = todayIncome,
            todayExpense = todayExpense,
            onDismiss = { showWidgetShowcase = false },
            onAddIncomeClick = onAddIncomeClick,
            onAddExpenseClick = onAddExpenseClick,
            onTransferClick = onTransferClick
        )
    }
}

@Composable
private fun OptionRow(
    icon: ImageVector,
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
                .background(Sand),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = DeepGreen, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                fontFamily = PoppinsFontFamily,
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
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
                .background(SoftGreenLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = DeepGreen, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = TextPrimary
            )
            Text(
                text = desc,
                fontFamily = PoppinsFontFamily,
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
    }
}
