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
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.SwapHoriz
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import com.example.data.entity.AccountEntity
import com.example.data.entity.AccountType
import com.example.ui.components.Formatters
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
import com.example.ui.theme.SurfaceDark2
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TitaniumBorder
import com.example.ui.theme.TitaniumDarkCard
import com.example.ui.theme.TitaniumDivider
import com.example.ui.theme.TitaniumLightBg
import com.example.ui.theme.TitaniumTextPrimary
import com.example.ui.theme.TitaniumTextSecondary
import com.example.ui.theme.TitaniumTextTertiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsScreen(
    accounts: List<AccountEntity>,
    onAddAccount: (AccountEntity) -> Unit,
    onUpdateAccount: (AccountEntity) -> Unit,
    onDeleteAccount: (AccountEntity) -> Unit,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var accountToEdit by remember { mutableStateOf<AccountEntity?>(null) }
    var accountToDelete by remember { mutableStateOf<AccountEntity?>(null) }

    val totalBalance = accounts.filter { it.type != AccountType.CREDIT_CARD.name }.sumOf { it.currentBalance }
    val totalDebt = accounts.filter { it.type == AccountType.CREDIT_CARD.name }.sumOf { it.currentBalance }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = TitaniumLightBg,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "BILLETERAS Y BANCOS",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 0.8.sp,
                            color = TitaniumTextSecondary
                        )
                        Text(
                            text = "Mis Cuentas",
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
                        modifier = Modifier.testTag("btn_add_account_top")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Agregar cuenta",
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
                modifier = Modifier.testTag("fab_add_account")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Nueva Cuenta",
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

            // Summary Card (Titanium Dark Card)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = TitaniumDarkCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "PATRIMONIO EN CUENTAS",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 0.8.sp,
                            color = Color(0xFF8E8E93)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = Formatters.formatMoney(totalBalance),
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = Color.White,
                            letterSpacing = (-0.5).sp
                        )
                        if (totalDebt > 0) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF381E1E)
                            ) {
                                Text(
                                    text = "Deuda en tarjetas: -${Formatters.formatMoney(totalDebt)}",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = CoralRed,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Section label
            item {
                Text(
                    text = "Cuentas registradas (${accounts.size})",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TitaniumTextPrimary
                )
            }

            if (accounts.isEmpty()) {
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
                            Text(text = "💳", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No tienes cuentas registradas",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = TitaniumTextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Agrega tus billeteras como Efectivo, Nequi, Bancolombia...",
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
                                Text("+ Agregar cuenta", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                items(accounts, key = { it.id }) { acc ->
                    CupertinoAccountItemRow(
                        account = acc,
                        onEdit = { accountToEdit = acc },
                        onDelete = { accountToDelete = acc }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(90.dp))
            }
        }
    }

    if (showAddDialog) {
        CupertinoAccountDialog(
            account = null,
            onDismiss = { showAddDialog = false },
            onConfirm = {
                onAddAccount(it)
                showAddDialog = false
            }
        )
    }

    accountToEdit?.let { acc ->
        CupertinoAccountDialog(
            account = acc,
            onDismiss = { accountToEdit = null },
            onConfirm = {
                onUpdateAccount(it)
                accountToEdit = null
            }
        )
    }

    accountToDelete?.let { acc ->
        AlertDialog(
            onDismissRequest = { accountToDelete = null },
            containerColor = SurfaceWhite,
            title = { Text("Eliminar cuenta", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, color = CoralRed) },
            text = { Text("¿Deseas eliminar la cuenta \"${acc.name}\"?", fontFamily = PoppinsFontFamily, color = TitaniumTextSecondary) },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteAccount(acc)
                        accountToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoralRed)
                ) {
                    Text("Eliminar", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { accountToDelete = null }) {
                    Text("Cancelar", fontFamily = PoppinsFontFamily)
                }
            }
        )
    }
}

@Composable
private fun CupertinoAccountItemRow(
    account: AccountEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    val isCreditCard = account.type == AccountType.CREDIT_CARD.name

    val icon: ImageVector = when (account.iconName.lowercase()) {
        "efectivo", "cash" -> Icons.Default.Payments
        "nequi", "wallet" -> Icons.Default.Smartphone
        "tarjeta", "credit_card" -> Icons.Default.CreditCard
        "ahorro", "savings" -> Icons.Default.Savings
        else -> Icons.Default.SwapHoriz
    }

    val iconBg = when (account.iconName.lowercase()) {
        "tarjeta", "credit_card" -> CoralRedLight
        "efectivo", "cash" -> EmeraldGreenLight
        else -> ElectricBlueLight
    }

    val iconTint = when (account.iconName.lowercase()) {
        "tarjeta", "credit_card" -> CoralRed
        "efectivo", "cash" -> EmeraldGreen
        else -> ElectricBlue
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = account.name,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TitaniumTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (isCreditCard && account.creditLimit != null) {
                    Text(
                        text = "Límite: ${Formatters.formatMoney(account.creditLimit)}",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 11.sp,
                        color = TitaniumTextSecondary
                    )
                } else {
                    Text(
                        text = AccountType.valueOf(account.type).displayName,
                        fontFamily = PoppinsFontFamily,
                        fontSize = 11.sp,
                        color = TitaniumTextSecondary
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                val amountColor = if (isCreditCard) CoralRed else TitaniumTextPrimary
                val sign = if (isCreditCard) "- " else ""
                Text(
                    text = "$sign${Formatters.formatMoney(account.currentBalance)}",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = amountColor
                )
            }

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
    }
}

@Composable
fun CupertinoAccountDialog(
    account: AccountEntity?,
    onDismiss: () -> Unit,
    onConfirm: (AccountEntity) -> Unit
) {
    var name by remember { mutableStateOf(account?.name ?: "") }
    var balanceText by remember { mutableStateOf(account?.currentBalance?.toLong()?.let { Formatters.formatAmountInput(it.toString()) } ?: "") }
    var selectedType by remember {
        mutableStateOf(account?.type?.let { runCatching { AccountType.valueOf(it) }.getOrNull() } ?: AccountType.WALLET)
    }
    var creditLimitText by remember { mutableStateOf(account?.creditLimit?.toLong()?.let { Formatters.formatAmountInput(it.toString()) } ?: "") }

    val isEdit = account != null

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceWhite,
        title = {
            Text(
                text = if (isEdit) "Editar cuenta" else "Nueva cuenta",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TitaniumTextPrimary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre (ej. Nequi, Bancolombia)", color = Color(0xFFE2E8F0), fontWeight = FontWeight.SemiBold) },
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
                        focusedPlaceholderColor = Color(0xFF94A3B8),
                        unfocusedPlaceholderColor = Color(0xFF94A3B8),
                        cursorColor = GoldPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = balanceText,
                    onValueChange = { input -> balanceText = Formatters.formatAmountInput(input) },
                    label = { Text(if (selectedType == AccountType.CREDIT_CARD) "Deuda actual ($)" else "Saldo actual ($)", color = Color(0xFFE2E8F0), fontWeight = FontWeight.SemiBold) },
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
                        focusedPlaceholderColor = Color(0xFF94A3B8),
                        unfocusedPlaceholderColor = Color(0xFF94A3B8),
                        cursorColor = GoldPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                if (selectedType == AccountType.CREDIT_CARD) {
                    OutlinedTextField(
                        value = creditLimitText,
                        onValueChange = { input -> creditLimitText = Formatters.formatAmountInput(input) },
                        label = { Text("Límite de crédito ($)", color = Color(0xFFE2E8F0), fontWeight = FontWeight.SemiBold) },
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
                            focusedPlaceholderColor = Color(0xFF94A3B8),
                            unfocusedPlaceholderColor = Color(0xFF94A3B8),
                            cursorColor = GoldPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Text(
                    text = "Tipo de cuenta:",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = TitaniumTextSecondary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    AccountType.values().take(3).forEach { type ->
                        val isSelected = selectedType == type
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { selectedType = type },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) TitaniumDarkCard else TitaniumLightBg
                        ) {
                            Text(
                                text = type.displayName.take(8),
                                fontFamily = PoppinsFontFamily,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp,
                                color = if (isSelected) Color.White else TitaniumTextPrimary,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val bal = Formatters.parseAmountInput(balanceText)
                    val limit = if (creditLimitText.isNotBlank()) Formatters.parseAmountInput(creditLimitText) else null
                    val icon = when (selectedType) {
                        AccountType.CASH -> "cash"
                        AccountType.WALLET -> "wallet"
                        AccountType.CREDIT_CARD -> "credit_card"
                        AccountType.SAVINGS -> "savings"
                        AccountType.BANK -> "bank"
                    }
                    val entity = account?.copy(
                        name = name.trim(),
                        type = selectedType.name,
                        currentBalance = bal,
                        creditLimit = limit,
                        iconName = icon
                    ) ?: AccountEntity(
                        name = name.trim(),
                        type = selectedType.name,
                        currentBalance = bal,
                        creditLimit = limit,
                        iconName = icon
                    )
                    onConfirm(entity)
                },
                enabled = name.isNotBlank(),
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
