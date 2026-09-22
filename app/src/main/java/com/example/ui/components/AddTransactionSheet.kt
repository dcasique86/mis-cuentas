package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.AccountingType
import com.example.data.entity.FavoriteEntity
import com.example.data.entity.TransactionEntity
import com.example.ui.theme.Coral
import com.example.ui.theme.CoralLight
import com.example.ui.theme.Cream
import com.example.ui.theme.DeepGreen
import com.example.ui.theme.NeutralGray
import com.example.ui.theme.PoppinsFontFamily
import com.example.ui.theme.Sand
import com.example.ui.theme.SoftGreen
import com.example.ui.theme.SoftGreenLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

enum class TransactionSheetMode {
    EXPENSE, INCOME, TRANSFER
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionSheet(
    initialMode: TransactionSheetMode = TransactionSheetMode.EXPENSE,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    favorites: List<FavoriteEntity> = emptyList(),
    onSaveFavorite: ((FavoriteEntity) -> Unit)? = null,
    onDeleteFavorite: ((FavoriteEntity) -> Unit)? = null,
    onDismiss: () -> Unit,
    onSave: (TransactionEntity) -> Unit
) {
    var mode by remember { mutableStateOf(initialMode) }
    var amountText by remember { mutableStateOf("") }
    var concept by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("Efectivo") }
    var note by remember { mutableStateOf("") }
    var showMoreAccountingTypes by remember { mutableStateOf(false) }

    val expenseCategories = listOf(
        "Comida", "Transporte", "Arriendo", "Servicios",
        "Compras", "Proveedores", "Deudas", "Salud",
        "Entretenimiento", "Otros"
    )
    val incomeCategories = listOf(
        "Venta", "Trabajo", "Préstamo", "Cobro", "Transferencia", "Otro"
    )

    var selectedCategory by remember(mode) {
        mutableStateOf(
            if (mode == TransactionSheetMode.EXPENSE) "Comida"
            else if (mode == TransactionSheetMode.INCOME) "Venta"
            else "Transferencia"
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

    val quickConcepts = if (mode == TransactionSheetMode.EXPENSE) {
        listOf("Almuerzo", "Envío", "Transporte", "Café", "Mercado", "Chuzo", "Servicios")
    } else {
        listOf("Venta pantaloneta", "Licra", "Trabajo", "Abono", "Honorarios", "Préstamo")
    }

    val quickAmounts = listOf(5000, 10000, 20000, 50000, 100000)
    val paymentMethods = listOf("Efectivo", "Transferencia", "Nequi", "Tarjeta")

    val isExpense = mode == TransactionSheetMode.EXPENSE
    val themeAccentColor = if (isExpense) Coral else if (mode == TransactionSheetMode.INCOME) SoftGreen else DeepGreen
    val isFormValid = amountText.toDoubleOrNull()?.let { it > 0 } == true && concept.isNotBlank()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Cream,
        tonalElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header: Title & Close
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Registrar movimiento",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    color = TextPrimary
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Fast Mode Selector Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Sand)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Gasto Tab
                val isGastoActive = mode == TransactionSheetMode.EXPENSE
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isGastoActive) Coral else Color.Transparent)
                        .clickable {
                            mode = TransactionSheetMode.EXPENSE
                            selectedCategory = "Comida"
                            selectedAccountingType = AccountingType.GASTO
                        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "− Gasto",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = if (isGastoActive) Color.White else TextSecondary
                    )
                }

                // Ingreso Tab
                val isIngresoActive = mode == TransactionSheetMode.INCOME
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isIngresoActive) SoftGreen else Color.Transparent)
                        .clickable {
                            mode = TransactionSheetMode.INCOME
                            selectedCategory = "Venta"
                            selectedAccountingType = AccountingType.VENTA
                        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+ Ingreso",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = if (isIngresoActive) DeepGreen else TextSecondary
                    )
                }

                // Transferencia Tab
                val isTransferActive = mode == TransactionSheetMode.TRANSFER
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isTransferActive) DeepGreen else Color.Transparent)
                        .clickable {
                            mode = TransactionSheetMode.TRANSFER
                            selectedCategory = "Transferencia"
                            selectedAccountingType = AccountingType.TRANSFERENCIA
                        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⇄ Transferir",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = if (isTransferActive) Color.White else TextSecondary
                    )
                }
            }

            // Quick Favorites Row
            if (favorites.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "★ Favoritos rápidos",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = DeepGreen
                    )
                    Text(
                        text = "Prellena en 1 toque",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(favorites) { fav ->
                        val isCurrent = concept.equals(fav.name, ignoreCase = true)
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isCurrent) SoftGreenLight else Color.White,
                            border = BorderStroke(1.dp, if (isCurrent) SoftGreen else NeutralGray),
                            modifier = Modifier.clickable {
                                concept = fav.name
                                selectedCategory = fav.category
                                paymentMethod = fav.paymentMethod
                                mode = if (fav.isIncome) TransactionSheetMode.INCOME else TransactionSheetMode.EXPENSE
                                if (fav.amount != null && fav.amount > 0) {
                                    amountText = if (fav.amount % 1.0 == 0.0) {
                                        fav.amount.toInt().toString()
                                    } else {
                                        fav.amount.toString()
                                    }
                                }
                                runCatching {
                                    selectedAccountingType = AccountingType.valueOf(fav.type)
                                }.onFailure {
                                    selectedAccountingType = if (fav.isIncome) AccountingType.VENTA else AccountingType.GASTO
                                }
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "★ ${fav.name}",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 13.sp,
                                    color = DeepGreen
                                )
                                if (fav.amount != null && fav.amount > 0) {
                                    Text(
                                        text = "$ ${fav.amount.toInt()}",
                                        fontFamily = PoppinsFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = if (fav.isIncome) DeepGreen else Coral
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Big Amount Input ($ ________)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Monto",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "$",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        color = if (mode == TransactionSheetMode.EXPENSE) Coral else DeepGreen,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { input ->
                            // keep only digits
                            amountText = input.filter { it.isDigit() }
                        },
                        placeholder = {
                            Text(
                                "0",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp,
                                color = NeutralGray
                            )
                        },
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp,
                            color = TextPrimary,
                            textAlign = TextAlign.Start
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .testTag("amount_input")
                    )
                }

                // Quick amounts chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickAmounts.forEach { quickVal ->
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    val current = amountText.toLongOrNull() ?: 0L
                                    amountText = (current + quickVal).toString()
                                },
                            color = Sand
                        ) {
                            Text(
                                text = "+${Formatters.formatMoney(quickVal.toDouble())}",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 11.sp,
                                color = TextPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Concept Input
            Text(
                text = "Concepto",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = concept,
                onValueChange = { concept = it },
                placeholder = { Text("Ej. Almuerzo, Licra, Envío, Sueldo...", fontSize = 14.sp) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("concept_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = DeepGreen,
                    unfocusedBorderColor = NeutralGray
                ),
                shape = RoundedCornerShape(12.dp)
            )

            // Quick concept suggestion pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                quickConcepts.forEach { suggestion ->
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { concept = suggestion },
                        color = if (concept == suggestion) SoftGreenLight else Sand
                    ) {
                        Text(
                            text = suggestion,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = if (concept == suggestion) DeepGreen else TextSecondary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Categories
            Text(
                text = "Categoría",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            val currentCategories = if (mode == TransactionSheetMode.EXPENSE) expenseCategories else incomeCategories
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                currentCategories.forEach { cat ->
                    val isSelected = selectedCategory == cat
                    val bg = if (isSelected) DeepGreen else Color.White
                    val txt = if (isSelected) Color.White else TextPrimary
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { selectedCategory = cat },
                        color = bg,
                        shape = RoundedCornerShape(10.dp),
                        shadowElevation = if (isSelected) 2.dp else 0.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = CategoryIcons.getIcon(cat),
                                contentDescription = cat,
                                tint = if (isSelected) Color.White else DeepGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = cat,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                fontSize = 13.sp,
                                color = txt
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Payment Method
            Text(
                text = "Método de pago / cuenta",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                paymentMethods.forEach { method ->
                    val isSelected = paymentMethod == method
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { paymentMethod = method },
                        color = if (isSelected) DeepGreen else Color.White,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = method,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            fontSize = 12.sp,
                            color = if (isSelected) Color.White else TextPrimary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Advanced Accounting Type Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showMoreAccountingTypes = !showMoreAccountingTypes }
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tipo contable: ${selectedAccountingType.displayName}",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = DeepGreen
                )
                Text(
                    text = if (showMoreAccountingTypes) "Ocultar ▲" else "Cambiar ▼",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            if (showMoreAccountingTypes) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    AccountingType.values().forEach { acType ->
                        val isSelected = selectedAccountingType == acType
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { selectedAccountingType = acType },
                            color = if (isSelected) DeepGreen else Sand
                        ) {
                            Text(
                                text = acType.displayName,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                fontSize = 11.sp,
                                color = if (isSelected) Color.White else TextPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Optional Note
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                placeholder = { Text("Nota opcional...", fontSize = 13.sp) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = DeepGreen,
                    unfocusedBorderColor = NeutralGray
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Action Button: Fast Save
            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull() ?: 0.0
                    val isInc = when (mode) {
                        TransactionSheetMode.INCOME -> true
                        TransactionSheetMode.EXPENSE -> false
                        TransactionSheetMode.TRANSFER -> false
                    }
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
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (mode == TransactionSheetMode.EXPENSE) Coral else DeepGreen,
                    disabledContainerColor = NeutralGray
                )
            ) {
                Text(
                    text = "Guardar Movimiento",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }

            if (concept.isNotBlank() && onSaveFavorite != null) {
                Spacer(modifier = Modifier.height(8.dp))
                val isAlreadyFav = favorites.any { it.name.equals(concept.trim(), ignoreCase = true) }
                var savedFavFeedback by remember { mutableStateOf(false) }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            val parsedAmount = amountText.toDoubleOrNull()
                            val isInc = mode == TransactionSheetMode.INCOME
                            val fav = FavoriteEntity(
                                name = concept.trim(),
                                type = selectedAccountingType.name,
                                category = selectedCategory,
                                paymentMethod = paymentMethod,
                                amount = parsedAmount,
                                isIncome = isInc
                            )
                            onSaveFavorite(fav)
                            savedFavFeedback = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = SoftGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (savedFavFeedback) "✓ Guardado en favoritos"
                            else if (isAlreadyFav) "Actualizar favorito"
                            else "Guardar como favorito",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = DeepGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
