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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.TransactionEntity
import com.example.ui.components.Formatters
import com.example.ui.components.TransactionItemRow
import com.example.ui.theme.Coral
import com.example.ui.theme.Cream
import com.example.ui.theme.DeepGreen
import com.example.ui.theme.NeutralGray
import com.example.ui.theme.PoppinsFontFamily
import com.example.ui.theme.Sand
import com.example.ui.theme.SoftGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
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
    modifier: Modifier = Modifier
) {
    var isSearchExpanded by remember { mutableStateOf(false) }
    var selectedTransactionForDetails by remember { mutableStateOf<TransactionEntity?>(null) }
    var transactionToDelete by remember { mutableStateOf<TransactionEntity?>(null) }

    val filterOptions = listOf("Todos", "Ingresos", "Gastos", "Deudas", "Transferencias")

    // Group transactions by date
    val grouped = remember(transactions) {
        transactions.groupBy { tx -> Formatters.formatDateGroup(tx.timestamp) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Cream)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(14.dp))

        // Screen Top Bar: Title & Search Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Movimientos",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = TextPrimary
            )

            Row {
                IconButton(onClick = { isSearchExpanded = !isSearchExpanded }) {
                    Icon(
                        imageVector = if (isSearchExpanded) Icons.Default.Close else Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = TextPrimary
                    )
                }
            }
        }

        // Search Input (Collapsible)
        if (isSearchExpanded) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Buscar concepto, categoría, nota...") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_transactions_input"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = DeepGreen,
                    unfocusedBorderColor = NeutralGray
                ),
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Limpiar", tint = TextSecondary)
                        }
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Filter chips (Todos, Ingresos, Gastos, Deudas, Transferencias)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filterOptions.forEach { filter ->
                val isSelected = selectedFilter == filter
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onFilterSelect(filter) }
                        .testTag("filter_chip_$filter"),
                    color = if (isSelected) DeepGreen else Sand,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = filter,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        fontSize = 13.sp,
                        color = if (isSelected) Color.White else TextPrimary,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Transactions List Grouped by Date
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
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (searchQuery.isNotBlank()) "Prueba con otro término de búsqueda"
                        else "No hay registros en este filtro",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                grouped.forEach { (dateGroup, listForDate) ->
                    item(key = "header_$dateGroup") {
                        Text(
                            text = dateGroup,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp, start = 4.dp)
                        )
                    }

                    item(key = "card_$dateGroup") {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
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
                                            color = NeutralGray.copy(alpha = 0.5f),
                                            thickness = 0.6.dp,
                                            modifier = Modifier.padding(horizontal = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }

    // Details Dialog
    selectedTransactionForDetails?.let { tx ->
        AlertDialog(
            onDismissRequest = { selectedTransactionForDetails = null },
            containerColor = Cream,
            title = {
                Text(
                    text = tx.concept,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimary
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
                        color = if (tx.isIncome) DeepGreen else Coral
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    DetailRow(label = "Tipo", value = tx.type)
                    DetailRow(label = "Categoría", value = tx.category)
                    DetailRow(label = "Método", value = tx.paymentMethod)
                    DetailRow(label = "Fecha", value = fullDate)
                    if (tx.note.isNotBlank()) {
                        DetailRow(label = "Nota", value = tx.note)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedTransactionForDetails = null
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepGreen)
                ) {
                    Text("Cerrar", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        val toDelete = tx
                        selectedTransactionForDetails = null
                        transactionToDelete = toDelete
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Coral)
                ) {
                    Text("Eliminar")
                }
            }
        )
    }

    // Delete Confirmation
    transactionToDelete?.let { tx ->
        AlertDialog(
            onDismissRequest = { transactionToDelete = null },
            containerColor = Cream,
            title = { Text("¿Eliminar movimiento?", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold) },
            text = { Text("Se eliminará '${tx.concept}' de ${Formatters.formatMoney(tx.amount)}. Esta acción ajustará el saldo disponible.") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteTransaction(tx)
                        transactionToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Coral),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Eliminar", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { transactionToDelete = null },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Cancelar", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontFamily = PoppinsFontFamily,
            fontSize = 13.sp,
            color = TextSecondary
        )
        Text(
            text = value,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = TextPrimary
        )
    }
}
