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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.DebtEntity
import com.example.data.entity.DebtPaymentEntity
import com.example.ui.components.AddDebtDialog
import com.example.ui.components.DebtDetailDialog
import com.example.ui.components.Formatters
import com.example.ui.components.PayDebtDialog
import kotlinx.coroutines.flow.Flow
import androidx.compose.runtime.collectAsState
import java.text.SimpleDateFormat
import java.util.Locale
import com.example.ui.theme.Coral
import com.example.ui.theme.CoralLight
import com.example.ui.theme.Cream
import com.example.ui.theme.DeepGreen
import com.example.ui.theme.NeutralGray
import com.example.ui.theme.PoppinsFontFamily
import com.example.ui.theme.Sand
import com.example.ui.theme.SoftGreen
import com.example.ui.theme.SoftGreenLight
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun DebtsScreen(
    debts: List<DebtEntity>,
    initialTabIsIOwe: Boolean = true,
    getPaymentsForDebt: ((Long) -> Flow<List<DebtPaymentEntity>>)? = null,
    onAddDebt: (DebtEntity) -> Unit,
    onPayDebt: (debtId: Long, amount: Double, method: String, note: String) -> Unit,
    onDeleteDebt: (DebtEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIsIOwe by remember(initialTabIsIOwe) { mutableStateOf(initialTabIsIOwe) } // true: Por pagar, false: Por cobrar
    var showAddDebtDialog by remember { mutableStateOf(false) }
    var selectedDebtForPayment by remember { mutableStateOf<DebtEntity?>(null) }
    var selectedDebtForDetail by remember { mutableStateOf<DebtEntity?>(null) }

    val filteredDebts = debts.filter { it.isIOwe == selectedTabIsIOwe }
    val totalPending = filteredDebts.sumOf { it.remainingAmount }
    val totalPaid = filteredDebts.sumOf { it.paidAmount }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Cream)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(14.dp))

        // Top Bar: Title & + Add Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Deudas",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = TextPrimary
            )

            // Circle + Button in SoftGreen
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(SoftGreen)
                    .clickable { showAddDebtDialog = true }
                    .testTag("add_debt_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar deuda",
                    tint = DeepGreen,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Segmented Control: [Por pagar] [Por cobrar]
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Sand)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (selectedTabIsIOwe) DeepGreen else Color.Transparent)
                    .clickable { selectedTabIsIOwe = true }
                    .padding(vertical = 10.dp)
                    .testTag("tab_por_pagar"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Por pagar",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = if (selectedTabIsIOwe) Color.White else TextSecondary
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (!selectedTabIsIOwe) DeepGreen else Color.Transparent)
                    .clickable { selectedTabIsIOwe = false }
                    .padding(vertical = 10.dp)
                    .testTag("tab_por_cobrar"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Por cobrar",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = if (!selectedTabIsIOwe) Color.White else TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (selectedTabIsIOwe) "Total pendiente por pagar" else "Total pendiente por cobrar",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = Formatters.formatMoney(totalPending),
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = if (selectedTabIsIOwe) Coral else DeepGreen
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Sand
                ) {
                    Text(
                        text = "${filteredDebts.size} cuentas",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = TextPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Debts List
        if (filteredDebts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (selectedTabIsIOwe) "No tienes deudas por pagar registradas."
                    else "No tienes deudas por cobrar registradas.",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 14.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredDebts, key = { it.id }) { debt ->
                    DebtCardItem(
                        debt = debt,
                        onCardClick = { selectedDebtForDetail = debt },
                        onPayClick = { selectedDebtForPayment = debt }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }

    if (showAddDebtDialog) {
        AddDebtDialog(
            initialIsIOwe = selectedTabIsIOwe,
            onDismiss = { showAddDebtDialog = false },
            onConfirm = { newDebt ->
                onAddDebt(newDebt)
                showAddDebtDialog = false
            }
        )
    }

    selectedDebtForPayment?.let { debt ->
        PayDebtDialog(
            debt = debt,
            onDismiss = { selectedDebtForPayment = null },
            onConfirm = { amount, method, note ->
                onPayDebt(debt.id, amount, method, note)
                selectedDebtForPayment = null
            }
        )
    }

    selectedDebtForDetail?.let { debt ->
        val payments = if (getPaymentsForDebt != null) {
            getPaymentsForDebt(debt.id).collectAsState(initial = emptyList()).value
        } else emptyList()

        DebtDetailDialog(
            debt = debt,
            payments = payments,
            onDismiss = { selectedDebtForDetail = null },
            onRegisterAbonoClick = {
                val target = selectedDebtForDetail
                selectedDebtForDetail = null
                selectedDebtForPayment = target
            },
            onDeleteDebt = {
                onDeleteDebt(debt)
                selectedDebtForDetail = null
            }
        )
    }
}

@Composable
private fun DebtCardItem(
    debt: DebtEntity,
    onCardClick: () -> Unit,
    onPayClick: () -> Unit
) {
    val initial = debt.name.trim().take(1).uppercase()
    val isCreditCard = debt.name.contains("tarjeta", ignoreCase = true)
    val progress = if (debt.totalAmount > 0) (debt.paidAmount / debt.totalAmount).toFloat().coerceIn(0f, 1f) else 0f
    val dateFormat = remember { SimpleDateFormat("d MMM", Locale("es", "CO")) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onCardClick)
            .testTag("debt_card_${debt.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circle with Initial or Card Icon
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(if (debt.isIOwe) SoftGreenLight else CoralLight),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCreditCard) {
                        Icon(
                            imageVector = Icons.Default.CreditCard,
                            contentDescription = null,
                            tint = DeepGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = initial,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = if (debt.isIOwe) DeepGreen else Coral
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = debt.name,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = if (debt.isIOwe) "Debe: ${Formatters.formatMoney(debt.totalAmount)}" else "Prestado: ${Formatters.formatMoney(debt.totalAmount)}",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }

                // Action button: Abonar / Cobrar
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(onClick = onPayClick),
                    color = Sand
                ) {
                    Text(
                        text = if (debt.isIOwe) "Abonar" else "Cobrar",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = DeepGreen,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Subtitle stats: Pagado & Pendiente
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Pagado: ${Formatters.formatMoney(debt.paidAmount)}",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Text(
                    text = "Pendiente: ${Formatters.formatMoney(debt.remainingAmount)}",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = if (debt.remainingAmount <= 0) SuccessGreen else if (debt.isIOwe) Coral else DeepGreen
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (debt.isIOwe) SoftGreen else DeepGreen,
                trackColor = Sand
            )

            // Extra context: Last payment date and days pending
            if (debt.lastPaymentDate != null || (!debt.isIOwe && !debt.isSettled && debt.pendingDays > 0)) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (debt.lastPaymentDate != null) {
                        Text(
                            text = "Último abono: ${dateFormat.format(debt.lastPaymentDate)}",
                            fontFamily = PoppinsFontFamily,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    if (!debt.isIOwe && !debt.isSettled && debt.pendingDays > 0) {
                        Text(
                            text = "⏱ Pendiente hace ${debt.pendingDays}d",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp,
                            color = Coral
                        )
                    }
                }
            }
        }
    }
}
