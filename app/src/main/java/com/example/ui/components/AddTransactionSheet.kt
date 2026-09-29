package com.example.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Work
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.AccountEntity
import com.example.data.entity.AccountType
import com.example.data.entity.AccountingType
import com.example.data.entity.AutoRuleEntity
import com.example.data.entity.FavoriteEntity
import com.example.data.entity.TransactionEntity
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
import com.example.util.VoiceInputParser

enum class TransactionSheetMode {
    EXPENSE, INCOME, TRANSFER
}

data class CategoryItem(val name: String, val icon: ImageVector)
data class PaymentMethodItem(val name: String, val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddTransactionSheet(
    initialMode: TransactionSheetMode = TransactionSheetMode.EXPENSE,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    favorites: List<FavoriteEntity> = emptyList(),
    rules: List<AutoRuleEntity> = emptyList(),
    accounts: List<AccountEntity> = emptyList(),
    onSaveFavorite: ((FavoriteEntity) -> Unit)? = null,
    onDeleteFavorite: ((FavoriteEntity) -> Unit)? = null,
    onDismiss: () -> Unit,
    onSave: (TransactionEntity) -> Unit
) {
    var mode by remember {
        mutableStateOf(initialMode)
    }

    var amountText by remember { mutableStateOf("") }
    var concept by remember { mutableStateOf("") }
    var showVoiceDialog by remember { mutableStateOf(false) }
    var autoMatchedCategory by remember { mutableStateOf<String?>(null) }
    var paymentMethod by remember {
        mutableStateOf(if (initialMode == TransactionSheetMode.TRANSFER) "Transferencia" else "Efectivo")
    }
    var note by remember { mutableStateOf("") }

    val expenseCategories = listOf(
        CategoryItem("Comida", Icons.Default.Restaurant),
        CategoryItem("Transporte", Icons.Default.DirectionsCar),
        CategoryItem("Hogar", Icons.Default.Home),
        CategoryItem("Trabajo", Icons.Default.Work),
        CategoryItem("Tienda", Icons.Default.ShoppingCart),
        CategoryItem("Ropa", Icons.Default.Checkroom)
    )

    val incomeCategories = listOf(
        CategoryItem("Venta", Icons.Default.ShoppingBag),
        CategoryItem("Trabajo", Icons.Default.Work),
        CategoryItem("Cobro", Icons.Default.Payments),
        CategoryItem("Transferencia", Icons.Default.SwapHoriz),
        CategoryItem("Otro", Icons.Default.Home)
    )

    val paymentMethodsList = if (accounts.isNotEmpty()) {
        val mapped = accounts.map { acc ->
            val icon = when (acc.type) {
                AccountType.CASH.name -> Icons.Default.Payments
                AccountType.WALLET.name -> Icons.Default.Smartphone
                AccountType.BANK.name -> Icons.Default.SwapHoriz
                AccountType.CREDIT_CARD.name -> Icons.Default.CreditCard
                else -> Icons.Default.Payments
            }
            PaymentMethodItem(acc.name, icon)
        }
        if (mode == TransactionSheetMode.TRANSFER && mapped.none { it.name.contains("Transfer", ignoreCase = true) }) {
            listOf(PaymentMethodItem("Transferencia", Icons.Default.SwapHoriz)) + mapped
        } else {
            mapped
        }
    } else {
        listOf(
            PaymentMethodItem("Efectivo", Icons.Default.Payments),
            PaymentMethodItem("Nequi", Icons.Default.Smartphone),
            PaymentMethodItem("Bancolombia", Icons.Default.SwapHoriz),
            PaymentMethodItem("Tarjeta", Icons.Default.CreditCard)
        )
    }

    var selectedCategory by remember(mode) {
        mutableStateOf(
            if (mode == TransactionSheetMode.TRANSFER) "Transferencia"
            else if (mode == TransactionSheetMode.EXPENSE) "Comida"
            else "Venta"
        )
    }

    var selectedAccountingType by remember(mode) {
        mutableStateOf(
            when (mode) {
                TransactionSheetMode.EXPENSE -> AccountingType.GASTO
                TransactionSheetMode.INCOME -> AccountingType.VENTA
                TransactionSheetMode.TRANSFER -> AccountingType.TRANSFERENCIA
            }
        )
    }

    val quickAmounts = listOf(5000, 10000, 20000, 50000)
    val isFormValid = Formatters.parseAmountInput(amountText) > 0 && concept.isNotBlank()

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val isImeVisible = WindowInsets.isImeVisible

    // Intercept back gesture on Android phone so it hides the keyboard instead of closing the module
    BackHandler(enabled = true) {
        if (isImeVisible) {
            keyboardController?.hide()
            focusManager.clearFocus()
        } else if (amountText.isNotBlank() || concept.isNotBlank()) {
            // Keep sheet open to protect data while user is registering income/expense
            keyboardController?.hide()
            focusManager.clearFocus()
        } else {
            // Only dismiss when empty and keyboard is already hidden
            onDismiss()
        }
    }

    ModalBottomSheet(
        onDismissRequest = {
            if (isImeVisible) {
                keyboardController?.hide()
                focusManager.clearFocus()
            } else {
                onDismiss()
            }
        },
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false
        ),
        sheetState = sheetState,
        containerColor = TitaniumLightBg,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 4.dp)
                    .width(42.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFFC7C7CC))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
                .padding(horizontal = 20.dp, vertical = 6.dp)
        ) {
            // Header: Title & Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nuevo Registro",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = TitaniumTextPrimary
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = TitaniumTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // -------------------------------------------------------------
            // Mode Selector: [↓ Gasto] [↑ Ingreso] [⇄ Traslado]
            // -------------------------------------------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceWhite)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // 1. Gasto
                val isGasto = mode == TransactionSheetMode.EXPENSE
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isGasto) CoralRedLight else Color.Transparent)
                        .clickable {
                            mode = TransactionSheetMode.EXPENSE
                            selectedCategory = "Comida"
                            selectedAccountingType = AccountingType.GASTO
                        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = null,
                            tint = if (isGasto) CoralRed else TitaniumTextSecondary,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Gasto",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isGasto) CoralRed else TitaniumTextSecondary
                        )
                    }
                }

                // 2. Ingreso
                val isIngreso = mode == TransactionSheetMode.INCOME
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isIngreso) EmeraldGreenLight else Color.Transparent)
                        .clickable {
                            mode = TransactionSheetMode.INCOME
                            selectedCategory = "Venta"
                            selectedAccountingType = AccountingType.VENTA
                        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = if (isIngreso) EmeraldGreen else TitaniumTextSecondary,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Ingreso",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isIngreso) EmeraldGreen else TitaniumTextSecondary
                        )
                    }
                }

                // 3. Traslado
                val isTransfer = mode == TransactionSheetMode.TRANSFER
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isTransfer) ElectricBlueLight else Color.Transparent)
                        .clickable {
                            mode = TransactionSheetMode.TRANSFER
                            selectedCategory = "Transferencia"
                            selectedAccountingType = AccountingType.TRANSFERENCIA
                            paymentMethod = "Transferencia"
                        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = null,
                            tint = if (isTransfer) ElectricBlue else TitaniumTextSecondary,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Traslado",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isTransfer) ElectricBlue else TitaniumTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // -------------------------------------------------------------
            // Amount Container Card (White Card with Border)
            // -------------------------------------------------------------
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                border = BorderStroke(1.dp, TitaniumBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "IMPORTE TOTAL",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 0.8.sp,
                            color = TitaniumTextSecondary
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = TitaniumLightBg,
                                modifier = Modifier.clickable {
                                    focusManager.clearFocus()
                                    keyboardController?.hide()
                                }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = ElectricBlue,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = "Listo",
                                        fontFamily = PoppinsFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = ElectricBlue
                                    )
                                }
                            }
                            Text(
                                text = "COP",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = TitaniumTextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "$",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 34.sp,
                                color = TitaniumTextPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            BasicTextField(
                                value = amountText,
                                onValueChange = { input ->
                                    amountText = Formatters.formatAmountInput(input)
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        focusManager.moveFocus(FocusDirection.Down)
                                    }
                                ),
                                singleLine = true,
                                textStyle = TextStyle(
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 36.sp,
                                    color = TitaniumTextPrimary
                                ),
                                cursorBrush = SolidColor(ElectricBlue),
                                decorationBox = { innerTextField ->
                                    if (amountText.isEmpty()) {
                                        Text(
                                            text = "0",
                                            fontFamily = PoppinsFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 36.sp,
                                            color = Color(0xFFC7C7CC)
                                        )
                                    }
                                    innerTextField()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("amount_input")
                            )
                        }

                        if (amountText.isNotEmpty()) {
                            IconButton(
                                onClick = { amountText = "" },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Cancel,
                                    contentDescription = "Borrar monto",
                                    tint = TitaniumTextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Quick amounts pills (+ $ 5.000, + $ 10.000, + $ 20.000, + $ 50.000)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        quickAmounts.forEach { quickVal ->
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        val current = Formatters.parseAmountInput(amountText)
                                        amountText = Formatters.formatAmountInput((current + quickVal).toLong().toString())
                                    },
                                shape = RoundedCornerShape(12.dp),
                                color = TitaniumLightBg
                            ) {
                                Text(
                                    text = "+ ${Formatters.formatMoney(quickVal.toDouble())}",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = TitaniumTextPrimary,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // -------------------------------------------------------------
            // Concepto
            // -------------------------------------------------------------
            Text(
                text = "Concepto",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = TitaniumTextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SurfaceWhite,
                border = BorderStroke(1.dp, TitaniumBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Notes,
                        contentDescription = null,
                        tint = TitaniumTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    BasicTextField(
                        value = concept,
                        onValueChange = { input ->
                            concept = input
                            val matched = VoiceInputParser.matchCategory(input, rules)
                            if (matched != null) {
                                selectedCategory = matched.targetCategory
                                autoMatchedCategory = matched.targetCategory
                                if (matched.isIncome) {
                                    mode = TransactionSheetMode.INCOME
                                }
                            } else {
                                autoMatchedCategory = null
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }
                        ),
                        singleLine = true,
                        textStyle = TextStyle(
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            color = TitaniumTextPrimary
                        ),
                        cursorBrush = SolidColor(ElectricBlue),
                        decorationBox = { innerTextField ->
                            if (concept.isEmpty()) {
                                Text(
                                    text = "Ej. Almuerzo, Uber, Envío, Netflix...",
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = 13.sp,
                                    color = Color(0xFFAEAEB2)
                                )
                            }
                            innerTextField()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("concept_input")
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (concept.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = "Borrar",
                                tint = TitaniumTextSecondary,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable {
                                        concept = ""
                                        autoMatchedCategory = null
                                    }
                            )
                        }
                        IconButton(
                            onClick = { showVoiceDialog = true },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Dictar por voz",
                                tint = ElectricBlue,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            if (!autoMatchedCategory.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Text(
                        text = "⚡ Detección automática:",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ElectricBlue
                    )
                    Text(
                        text = "Asignado a '$autoMatchedCategory'",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 11.sp,
                        color = TitaniumTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // -------------------------------------------------------------
            // Categoría
            // -------------------------------------------------------------
            Text(
                text = "Categoría",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = TitaniumTextPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))

            val currentCategories = if (mode == TransactionSheetMode.EXPENSE) expenseCategories else incomeCategories
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                currentCategories.forEach { catItem ->
                    val isSelected = selectedCategory == catItem.name
                    val cardBg = if (isSelected) TitaniumDarkCard else SurfaceWhite
                    val tintColor = if (isSelected) Color.White else TitaniumTextSecondary
                    val textColor = if (isSelected) Color.White else TitaniumTextPrimary

                    Surface(
                        modifier = Modifier
                            .size(width = 68.dp, height = 68.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { selectedCategory = catItem.name },
                        shape = RoundedCornerShape(16.dp),
                        color = cardBg,
                        border = if (!isSelected) BorderStroke(1.dp, TitaniumBorder) else null
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = catItem.icon,
                                contentDescription = catItem.name,
                                tint = tintColor,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = catItem.name,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 11.sp,
                                color = textColor
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // -------------------------------------------------------------
            // Método de pago / cuenta
            // -------------------------------------------------------------
            Text(
                text = "Método de pago / cuenta",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = TitaniumTextPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                paymentMethodsList.forEach { pItem ->
                    val isSelected = paymentMethod == pItem.name
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { paymentMethod = pItem.name },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) TitaniumDarkCard else SurfaceWhite,
                        border = if (!isSelected) BorderStroke(1.dp, TitaniumBorder) else null
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = pItem.icon,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else TitaniumTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = pItem.name,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp,
                                color = if (isSelected) Color.White else TitaniumTextPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // -------------------------------------------------------------
            // Guardar Movimiento (Titanium Dark Capsule Button)
            // -------------------------------------------------------------
            Button(
                onClick = {
                    val amount = Formatters.parseAmountInput(amountText)
                    val isInc = mode == TransactionSheetMode.INCOME
                    val tx = TransactionEntity(
                        type = selectedAccountingType.name,
                        concept = concept.trim(),
                        category = selectedCategory,
                        paymentMethod = paymentMethod,
                        amount = amount,
                        isIncome = isInc,
                        timestamp = System.currentTimeMillis(),
                        note = note.trim()
                    )
                    onSave(tx)
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_transaction_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TitaniumDarkCard,
                    disabledContainerColor = Color(0xFFC7C7CC)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Guardar Movimiento",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
        }
    }

    if (showVoiceDialog) {
        VoiceInputDialog(
            rules = rules,
            onDismiss = { showVoiceDialog = false },
            onConfirm = { parsedTx, chosenPaymentMethod ->
                if (parsedTx.amount > 0) {
                    amountText = Formatters.formatAmountInput(parsedTx.amount.toLong().toString())
                }
                concept = parsedTx.concept
                selectedCategory = parsedTx.category
                autoMatchedCategory = parsedTx.category
                mode = if (parsedTx.isIncome) TransactionSheetMode.INCOME else TransactionSheetMode.EXPENSE
                paymentMethod = chosenPaymentMethod
                showVoiceDialog = false
            }
        )
    }
}
