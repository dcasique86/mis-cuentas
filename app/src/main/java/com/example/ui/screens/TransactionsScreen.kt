package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.TransactionEntity
import com.example.ui.components.Formatters
import com.example.ui.components.TransactionItemRow
import com.example.ui.theme.CoralRed
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueLight
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.PoppinsFontFamily
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TitaniumBorder
import com.example.ui.theme.TitaniumDarkCard
import com.example.ui.theme.TitaniumDivider
import com.example.ui.theme.TitaniumLightBg
import com.example.ui.theme.TitaniumTextPrimary
import com.example.ui.theme.TitaniumTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransactionsScreen(
    transactions: List<TransactionEntity>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedFilter: String,
    onFilterSelect: (String) -> Unit,
    onDeleteTransaction: (TransactionEntity) -> Unit,
    onAddTransactionClick: (() -> Unit)? = null,
    onVoiceInputClick: (() -> Unit)? = null,
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var selectedTransactionForDetails by remember { mutableStateOf<TransactionEntity?>(null) }
    var transactionToDelete by remember { mutableStateOf<TransactionEntity?>(null) }

    val filterOptions = listOf("Todos", "Ingresos", "Gastos", "Traslados")

    // Group transactions by date
    val grouped = remember(transactions) {
        transactions.groupBy { tx -> Formatters.formatDateGroup(tx.timestamp) }
    }

    // Monthly summary calculation for banner
    val totalEntradas = remember(transactions) {
        transactions.filter { it.isIncome }.sumOf { it.amount }
    }
    val totalSalidas = remember(transactions) {
        transactions.filter { !it.isIncome && it.type != com.example.data.entity.AccountingType.TRANSFERENCIA.name }.sumOf { it.amount }
    }
    val balanceMes = totalEntradas - totalSalidas

    val currentMonthYear = remember {
        SimpleDateFormat("MMMM yyyy", Locale("es", "CO")).format(Date()).uppercase(Locale("es", "CO"))
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = TitaniumLightBg,
        floatingActionButton = {
            if (onAddTransactionClick != null) {
                ExtendedFloatingActionButton(
                    onClick = onAddTransactionClick,
                    containerColor = ElectricBlue,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    elevation = androidx.compose.material3.FloatingActionButtonDefaults.elevation(3.dp),
                    modifier = Modifier.testTag("fab_nuevo_movimiento")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Nuevo Movimiento",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(14.dp))

            // -------------------------------------------------------------
            // Top Bar: Title & Voice / Profile actions
            // -------------------------------------------------------------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (onBackClick != null) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .size(40.dp)
                                .background(SurfaceWhite, CircleShape)
                                .testTag("transactions_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver a Inicio",
                                tint = TitaniumTextPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                    Column {
                        Text(
                            text = "MIS CUENTAS",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 0.8.sp,
                            color = TitaniumTextSecondary
                        )
                        Text(
                            text = "Movimientos",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = TitaniumTextPrimary
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (onVoiceInputClick != null) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = ElectricBlueLight,
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { onVoiceInputClick() }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "Voz",
                                    tint = ElectricBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Voz",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = ElectricBlue
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // -------------------------------------------------------------
            // Search Input (Cupertino Capsule style)
            // -------------------------------------------------------------
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = {
                    Text(
                        text = "Buscar café, arriendo, cliente...",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 13.sp,
                        color = Color(0xFFAEAEB2)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Color(0xFF8E8E93),
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpiar",
                                tint = TitaniumTextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_transactions_input"),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceWhite,
                    unfocusedContainerColor = SurfaceWhite,
                    focusedBorderColor = ElectricBlue,
                    unfocusedBorderColor = TitaniumBorder,
                    focusedTextColor = TitaniumTextPrimary,
                    unfocusedTextColor = TitaniumTextPrimary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // -------------------------------------------------------------
            // Filter Chips (Todos, Ingresos, Gastos, Traslados)
            // -------------------------------------------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filterOptions.forEach { filter ->
                    val isSelected = selectedFilter == filter || (filter == "Traslados" && selectedFilter == "Transferencias")
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                if (filter == "Traslados") onFilterSelect("Transferencias")
                                else onFilterSelect(filter)
                            }
                            .testTag("filter_chip_$filter"),
                        color = if (isSelected) TitaniumDarkCard else SurfaceWhite,
                        shape = RoundedCornerShape(12.dp),
                        border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, TitaniumBorder) else null
                    ) {
                        Text(
                            text = filter,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp,
                            color = if (isSelected) Color.White else TitaniumTextPrimary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // -------------------------------------------------------------
            // Monthly Summary Card (Cupertino White Card)
            // -------------------------------------------------------------
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = null,
                                tint = TitaniumTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = currentMonthYear,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = TitaniumTextPrimary
                            )
                        }

                        Text(
                            text = "Actualizado hoy",
                            fontFamily = PoppinsFontFamily,
                            fontSize = 11.sp,
                            color = TitaniumTextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Entradas
                        Column {
                            Text(
                                text = "ENTRADAS",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                letterSpacing = 0.6.sp,
                                color = TitaniumTextSecondary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "+${Formatters.formatMoney(totalEntradas)}",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = EmeraldGreen
                            )
                        }

                        // Salidas
                        Column {
                            Text(
                                text = "SALIDAS",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                letterSpacing = 0.6.sp,
                                color = TitaniumTextSecondary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "-${Formatters.formatMoney(totalSalidas)}",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = CoralRed
                            )
                        }

                        // Balance
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "BALANCE",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                letterSpacing = 0.6.sp,
                                color = TitaniumTextSecondary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            val balSign = if (balanceMes >= 0) "+" else "-"
                            Text(
                                text = "$balSign${Formatters.formatMoney(kotlin.math.abs(balanceMes))}",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = ElectricBlue
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // -------------------------------------------------------------
            // Transactions List Grouped by Date
            // -------------------------------------------------------------
            if (transactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No se encontraron movimientos",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = TitaniumTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (searchQuery.isNotBlank()) "Prueba con otro término de búsqueda"
                            else "No hay registros en este filtro",
                            fontFamily = PoppinsFontFamily,
                            fontSize = 13.sp,
                            color = TitaniumTextSecondary
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    grouped.forEach { (dateGroup, listForDate) ->
                        item(key = "header_$dateGroup") {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp, vertical = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = dateGroup.uppercase(Locale("es", "CO")),
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    letterSpacing = 0.6.sp,
                                    color = TitaniumTextSecondary
                                )
                                Text(
                                    text = "${listForDate.size} ${if (listForDate.size == 1) "transacción" else "transacciones"}",
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = 11.sp,
                                    color = TitaniumTextSecondary
                                )
                            }
                        }

                        item(key = "card_$dateGroup") {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)) {
                                    listForDate.forEachIndexed { index, tx ->
                                        TransactionItemRow(
                                            transaction = tx,
                                            onClick = { selectedTransactionForDetails = tx }
                                        )
                                        if (index < listForDate.size - 1) {
                                            HorizontalDivider(
                                                color = TitaniumDivider,
                                                thickness = 0.8.dp,
                                                modifier = Modifier.padding(horizontal = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(90.dp))
                    }
                }
            }
        }
    }

    // -------------------------------------------------------------
    // Transaction Details Dialog (Cupertino White Dialog)
    // -------------------------------------------------------------
    selectedTransactionForDetails?.let { tx ->
        AlertDialog(
            onDismissRequest = { selectedTransactionForDetails = null },
            containerColor = SurfaceWhite,
            shape = RoundedCornerShape(24.dp),
            title = {
                Text(
                    text = tx.concept,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TitaniumTextPrimary
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    val fullDate = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("es", "CO")).format(Date(tx.timestamp))
                    Text(
                        text = Formatters.formatMoney(tx.amount),
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = if (tx.isIncome) EmeraldGreen else CoralRed
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Fecha: $fullDate",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 12.sp,
                        color = TitaniumTextSecondary
                    )
                    Text(
                        text = "Categoría: ${tx.category}",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 12.sp,
                        color = TitaniumTextSecondary
                    )
                    Text(
                        text = "Método de pago: ${tx.paymentMethod}",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 12.sp,
                        color = TitaniumTextSecondary
                    )

                    if (tx.note.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Nota: ${tx.note}",
                            fontFamily = PoppinsFontFamily,
                            fontSize = 12.sp,
                            color = TitaniumTextSecondary
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        transactionToDelete = tx
                        selectedTransactionForDetails = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoralRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Eliminar",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { selectedTransactionForDetails = null },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Cerrar",
                        fontFamily = PoppinsFontFamily,
                        color = TitaniumTextPrimary
                    )
                }
            }
        )
    }

    // Delete Confirmation Dialog
    transactionToDelete?.let { tx ->
        AlertDialog(
            onDismissRequest = { transactionToDelete = null },
            containerColor = SurfaceWhite,
            shape = RoundedCornerShape(24.dp),
            title = {
                Text(
                    text = "¿Eliminar movimiento?",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = TitaniumTextPrimary
                )
            },
            text = {
                Text(
                    text = "Se eliminará '${tx.concept}' por ${Formatters.formatMoney(tx.amount)} y se ajustará el saldo automáticamente.",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 13.sp,
                    color = TitaniumTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteTransaction(tx)
                        transactionToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoralRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Sí, eliminar",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { transactionToDelete = null },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Cancelar",
                        fontFamily = PoppinsFontFamily,
                        color = TitaniumTextPrimary
                    )
                }
            }
        )
    }
}
