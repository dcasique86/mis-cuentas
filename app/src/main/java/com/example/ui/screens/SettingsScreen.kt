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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.AccountEntity
import com.example.data.entity.AccountType
import com.example.ui.components.Formatters
import com.example.ui.components.MisCuentasIcon
import com.example.ui.components.WidgetShowcaseDialog
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentBalance: Double,
    onBackClick: () -> Unit,
    onAdjustCash: (newBalance: Double, note: String) -> Unit,
    onResetDemoData: () -> Unit,
    onClearDatabase: () -> Unit,
    onDailyClosingClick: () -> Unit,
    accounts: List<AccountEntity> = emptyList(),
    onNavigateToAccounts: () -> Unit = {},
    onAddAccount: (AccountEntity) -> Unit = {},
    onUpdateAccount: (AccountEntity) -> Unit = {},
    onDeleteAccount: (AccountEntity) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Dialog states
    var showAdjustDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showClearDbDialog by remember { mutableStateOf(false) }
    var showCurrencyDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showWidgetShowcase by remember { mutableStateOf(false) }

    // Setting states
    var selectedCurrency by remember { mutableStateOf("COP ($)") }
    var defaultPaymentMethod by remember { mutableStateOf("Efectivo") }
    var dailyReminderEnabled by remember { mutableStateOf(true) }
    var hapticFeedbackEnabled by remember { mutableStateOf(true) }
    var adjustAmountText by remember { mutableStateOf("") }
    var adjustNote by remember { mutableStateOf("Ajuste manual de saldo") }

    // Account editing states
    var accountToEdit by remember { mutableStateOf<AccountEntity?>(null) }
    var showAddAccountDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = TitaniumLightBg,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "PREFERENCIAS",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 0.8.sp,
                            color = TitaniumTextSecondary
                        )
                        Text(
                            text = "Configuración",
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
                        modifier = Modifier.testTag("settings_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = TitaniumTextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TitaniumLightBg
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // App Identity Card (Titanium Dark Card)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = TitaniumDarkCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MisCuentasIcon(size = 46.dp)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Mis Cuentas",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Versión 1.2 · Tu dinero, en orden",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            color = ElectricBlue
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECTION 1: PREFERENCIAS GENERALES
            Text(
                text = "Preferencias generales",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = TitaniumTextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    // Moneda principal
                    SettingsItemRow(
                        icon = Icons.Default.AttachMoney,
                        title = "Moneda principal",
                        subtitle = "Actual: $selectedCurrency (Separador con punto)",
                        onClick = { showCurrencyDialog = true }
                    )

                    HorizontalDivider(color = TitaniumDivider, thickness = 0.8.dp)

                    // Método de pago por defecto
                    SettingsItemRow(
                        icon = Icons.Default.CreditCard,
                        title = "Método de pago predeterminado",
                        subtitle = defaultPaymentMethod,
                        onClick = {
                            defaultPaymentMethod = when (defaultPaymentMethod) {
                                "Efectivo" -> "Transferencia"
                                "Transferencia" -> "Nequi"
                                "Nequi" -> "Tarjeta"
                                else -> "Efectivo"
                            }
                        }
                    )

                    HorizontalDivider(color = TitaniumDivider, thickness = 0.8.dp)

                    // Recordatorio diario de cierre
                    SettingsSwitchRow(
                        icon = Icons.Default.Notifications,
                        title = "Recordatorio de cierre diario",
                        subtitle = "Aviso nocturno para contar el efectivo",
                        checked = dailyReminderEnabled,
                        onCheckedChange = { dailyReminderEnabled = it }
                    )

                    HorizontalDivider(color = TitaniumDivider, thickness = 0.8.dp)

                    // Vibración / Retroalimentación háptica
                    SettingsSwitchRow(
                        icon = Icons.Default.Tune,
                        title = "Vibración al registrar",
                        subtitle = "Confirmación táctil al guardar movimiento",
                        checked = hapticFeedbackEnabled,
                        onCheckedChange = { hapticFeedbackEnabled = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECTION: CUENTAS Y TARJETAS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cuentas y Tarjetas",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TitaniumTextPrimary
                )
                Text(
                    text = "Gestionar todo",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = ElectricBlue,
                    modifier = Modifier.clickable { onNavigateToAccounts() }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    // Navigate to Accounts Screen
                    SettingsItemRow(
                        icon = Icons.Default.AccountBalance,
                        title = "Administrar Cuentas y Tarjetas",
                        subtitle = "Efectivo, Nequi, Bancolombia y más (${accounts.size} activas)",
                        onClick = onNavigateToAccounts
                    )

                    if (accounts.isNotEmpty()) {
                        HorizontalDivider(color = TitaniumDivider, thickness = 0.8.dp)

                        accounts.forEachIndexed { index, account ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { accountToEdit = account }
                                    .padding(vertical = 11.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    val icon = when (account.type) {
                                        AccountType.CASH.name -> Icons.Default.Payments
                                        AccountType.WALLET.name -> Icons.Default.Smartphone
                                        AccountType.BANK.name -> Icons.Default.AccountBalance
                                        AccountType.CREDIT_CARD.name -> Icons.Default.CreditCard
                                        AccountType.SAVINGS.name -> Icons.Default.Savings
                                        else -> Icons.Default.Payments
                                    }
                                    Surface(
                                        shape = CircleShape,
                                        color = TitaniumLightBg,
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = icon,
                                                contentDescription = null,
                                                tint = TitaniumTextPrimary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                    Column {
                                        Text(
                                            text = account.name,
                                            fontFamily = PoppinsFontFamily,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.sp,
                                            color = TitaniumTextPrimary
                                        )
                                        Text(
                                            text = Formatters.formatMoney(account.currentBalance),
                                            fontFamily = PoppinsFontFamily,
                                            fontSize = 12.sp,
                                            color = TitaniumTextSecondary
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = TitaniumLightBg,
                                    modifier = Modifier.clickable { accountToEdit = account }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Editar",
                                            tint = ElectricBlue,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Text(
                                            text = "Editar",
                                            fontFamily = PoppinsFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = ElectricBlue
                                        )
                                    }
                                }
                            }

                            if (index < accounts.size - 1) {
                                HorizontalDivider(color = TitaniumDivider, thickness = 0.5.dp)
                            }
                        }
                    }

                    HorizontalDivider(color = TitaniumDivider, thickness = 0.8.dp)

                    // Button to add a new account
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showAddAccountDialog = true }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = ElectricBlue,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "+ Agregar Nueva Cuenta o Tarjeta",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = ElectricBlue
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECTION 2: WIDGETS Y PANTALLA DE INICIO
            Text(
                text = "Widgets y pantalla de inicio",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = TitaniumTextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    SettingsItemRow(
                        icon = Icons.Default.Widgets,
                        title = "Diseños de widgets (4x2, 2x2, 4x3)",
                        subtitle = "Ver y configurar tamaños adaptables para tu pantalla",
                        onClick = { showWidgetShowcase = true }
                    )

                    HorizontalDivider(color = TitaniumDivider, thickness = 0.8.dp)

                    SettingsItemRow(
                        icon = Icons.Default.ReceiptLong,
                        title = "Cierre del día",
                        subtitle = "Cuenta tu efectivo y verifica tu caja diaria",
                        onClick = onDailyClosingClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECTION 3: GESTIÓN DE CAJA Y DATOS
            Text(
                text = "Gestión de caja y base de datos",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = TitaniumTextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    // Ajustar saldo
                    SettingsItemRow(
                        icon = Icons.Default.Tune,
                        title = "Ajustar saldo real de caja",
                        subtitle = "Saldo actual registrado: ${Formatters.formatMoney(currentBalance)}",
                        onClick = {
                            adjustAmountText = currentBalance.toInt().toString()
                            showAdjustDialog = true
                        }
                    )

                    HorizontalDivider(color = TitaniumDivider, thickness = 0.8.dp)

                    // Cargar datos demo
                    SettingsItemRow(
                        icon = Icons.Default.Refresh,
                        title = "Cargar datos de prueba",
                        subtitle = "Añade ejemplos prácticos para explorar todas las vistas",
                        onClick = { showResetDialog = true }
                    )

                    HorizontalDivider(color = TitaniumDivider, thickness = 0.8.dp)

                    // Borrar base de datos
                    SettingsItemRow(
                        icon = Icons.Default.DeleteSweep,
                        title = "Borrar todos los datos (Reiniciar a $0)",
                        subtitle = "Deja la aplicación limpia para tu contabilidad real",
                        onClick = { showClearDbDialog = true },
                        titleColor = CoralRed
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECTION 4: INFORMACIÓN Y PRIVACIDAD
            Text(
                text = "Privacidad y seguridad",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = TitaniumTextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    SettingsItemRow(
                        icon = Icons.Default.Security,
                        title = "100% Offline y Privado",
                        subtitle = "Tus datos viven únicamente en este dispositivo. No hay servidores externos.",
                        onClick = { showAboutDialog = true }
                    )

                    HorizontalDivider(color = TitaniumDivider, thickness = 0.8.dp)

                    SettingsItemRow(
                        icon = Icons.Default.Info,
                        title = "Acerca de Mis Cuentas",
                        subtitle = "Desarrollado para registrar tus movimientos en 2-5 segundos",
                        onClick = { showAboutDialog = true }
                    )
                }
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }

    // DIALOG: Currency Selection
    if (showCurrencyDialog) {
        val currencies = listOf("COP ($) - Peso Colombiano", "USD ($) - Dólar", "EUR (€) - Euro", "MXN ($) - Peso Mexicano")
        AlertDialog(
            onDismissRequest = { showCurrencyDialog = false },
            containerColor = SurfaceWhite,
            title = {
                Text(
                    text = "Moneda del sistema",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = TitaniumTextPrimary
                )
            },
            text = {
                Column {
                    currencies.forEach { curr ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedCurrency = curr.substringBefore(" -")
                                    showCurrencyDialog = false
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedCurrency == curr.substringBefore(" -"),
                                onClick = {
                                    selectedCurrency = curr.substringBefore(" -")
                                    showCurrencyDialog = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = ElectricBlue)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = curr,
                                fontFamily = PoppinsFontFamily,
                                fontSize = 14.sp,
                                color = TitaniumTextPrimary
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCurrencyDialog = false }) {
                    Text("Cerrar", color = ElectricBlue, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // DIALOG: Adjust Cash
    if (showAdjustDialog) {
        AlertDialog(
            onDismissRequest = { showAdjustDialog = false },
            containerColor = SurfaceWhite,
            title = {
                Text(
                    text = "Ajustar saldo de caja",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = TitaniumTextPrimary
                )
            },
            text = {
                Column {
                    Text(
                        text = "Introduce el monto total real que tienes en efectivo:",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 13.sp,
                        color = TitaniumTextSecondary
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedTextField(
                        value = adjustAmountText,
                        onValueChange = { adjustAmountText = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Saldo real") },
                        prefix = { Text("$ ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = TitaniumBorder
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = adjustNote,
                        onValueChange = { adjustNote = it },
                        label = { Text("Motivo / Nota") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = TitaniumBorder
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = adjustAmountText.toDoubleOrNull() ?: currentBalance
                        onAdjustCash(amount, adjustNote)
                        showAdjustDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Guardar ajuste", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAdjustDialog = false }) {
                    Text("Cancelar", color = TitaniumTextSecondary)
                }
            }
        )
    }

    // DIALOG: Reset Demo Data
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            containerColor = SurfaceWhite,
            title = {
                Text(
                    text = "Cargar datos de ejemplo",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = TitaniumTextPrimary
                )
            },
            text = {
                Text(
                    text = "¿Deseas cargar datos de prueba para explorar gráficos, reportes, cierres de caja y deudas?",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 14.sp,
                    color = TitaniumTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onResetDemoData()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cargar datos", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showResetDialog = false }) {
                    Text("Cancelar", color = TitaniumTextSecondary)
                }
            }
        )
    }

    // DIALOG: Clear DB
    if (showClearDbDialog) {
        AlertDialog(
            onDismissRequest = { showClearDbDialog = false },
            containerColor = SurfaceWhite,
            title = {
                Text(
                    text = "¿Reiniciar a $0?",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = CoralRed
                )
            },
            text = {
                Text(
                    text = "Esta acción eliminará todos los movimientos, deudas y cierres registrados para dejar tu contabilidad completamente limpia.",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 14.sp,
                    color = TitaniumTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onClearDatabase()
                        showClearDbDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoralRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Sí, vaciar todo", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showClearDbDialog = false }) {
                    Text("Cancelar", color = TitaniumTextSecondary)
                }
            }
        )
    }

    // DIALOG: About App
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            containerColor = SurfaceWhite,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MisCuentasIcon(size = 32.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Mis Cuentas", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, color = TitaniumTextPrimary)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Tu dinero, en orden.",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = ElectricBlue,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Diseñada para que registrar tus ingresos y gastos diarios tome entre 2 y 5 segundos, sin menús complicados ni términos bancarios enredados.",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 13.sp,
                        color = TitaniumTextSecondary,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "• Privacidad total: Tus finanzas nunca salen de tu teléfono.\n• Separador colombiano de miles con puntos.\n• Widgets interactivos con ajuste automático de tamaño.",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 12.sp,
                        color = TitaniumTextSecondary,
                        lineHeight = 16.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showAboutDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Entendido", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // DIALOG: Widgets showcase
    if (showWidgetShowcase) {
        WidgetShowcaseDialog(
            balance = currentBalance,
            todayIncome = 0.0,
            todayExpense = 0.0,
            onDismiss = { showWidgetShowcase = false },
            onAddIncomeClick = {},
            onAddExpenseClick = {},
            onTransferClick = {}
        )
    }

    // DIALOG: Edit Account
    if (accountToEdit != null) {
        CupertinoAccountDialog(
            account = accountToEdit,
            onDismiss = { accountToEdit = null },
            onConfirm = { updated ->
                onUpdateAccount(updated)
                accountToEdit = null
            }
        )
    }

    // DIALOG: Add Account
    if (showAddAccountDialog) {
        CupertinoAccountDialog(
            account = null,
            onDismiss = { showAddAccountDialog = false },
            onConfirm = { newAccount ->
                onAddAccount(newAccount)
                showAddAccountDialog = false
            }
        )
    }
}

@Composable
private fun SettingsItemRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    titleColor: Color = TitaniumTextPrimary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(if (titleColor == CoralRed) CoralRedLight else ElectricBlueLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (titleColor == CoralRed) CoralRed else ElectricBlue,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = titleColor
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
private fun SettingsSwitchRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(ElectricBlueLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = ElectricBlue,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
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
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = ElectricBlue,
                uncheckedTrackColor = Color(0xFFE5E5EA)
            )
        )
    }
}
