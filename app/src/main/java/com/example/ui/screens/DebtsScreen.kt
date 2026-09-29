package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DebtsScreen(
    debts: List<DebtEntity>,
    initialTabIsIOwe: Boolean = true,
    getPaymentsForDebt: ((Long) -> Flow<List<DebtPaymentEntity>>)? = null,
    onAddDebt: (DebtEntity) -> Unit,
    onPayDebt: (debtId: Long, amount: Double, method: String, note: String) -> Unit,
    onDeleteDebt: (DebtEntity) -> Unit,
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    // selectedTabIsIOwe: true -> "Debo" (Por pagar), false -> "Me deben" (Por cobrar)
    var selectedTabIsIOwe by remember(initialTabIsIOwe) { mutableStateOf(initialTabIsIOwe) }
    var showAddDebtDialog by remember { mutableStateOf(false) }
    var selectedDebtForPayment by remember { mutableStateOf<DebtEntity?>(null) }
    var selectedDebtForDetail by remember { mutableStateOf<DebtEntity?>(null) }

    val filteredDebts = debts.filter { it.isIOwe == selectedTabIsIOwe }
    val totalPending = filteredDebts.sumOf { it.remainingAmount }
    val totalOriginal = filteredDebts.sumOf { it.totalAmount }
    val totalPaid = filteredDebts.sumOf { it.paidAmount }

    val countMeDeben = debts.count { !it.isIOwe }
    val countDebo = debts.count { it.isIOwe }

    val recoveryPercentage = if (totalOriginal > 0) ((totalPaid / totalOriginal) * 100).toInt() else 0

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = TitaniumLightBg,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDebtDialog = true },
                containerColor = ElectricBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                elevation = androidx.compose.material3.FloatingActionButtonDefaults.elevation(3.dp),
                modifier = Modifier.testTag("add_debt_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (selectedTabIsIOwe) "Nueva Deuda" else "Nuevo Préstamo",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
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
            // Top Bar: Title & Subtitle
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
                                .testTag("debts_back_button")
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
                            text = "PRÉSTAMOS Y COBROS",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 0.8.sp,
                            color = TitaniumTextSecondary
                        )
                        Text(
                            text = "Deudas",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = TitaniumTextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // -------------------------------------------------------------
            // Segmented Control: [↑ Me deben 4] [↓ Debo 2]
            // -------------------------------------------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceWhite)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Tab: Me deben (Por cobrar)
                val isMeDeben = !selectedTabIsIOwe
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isMeDeben) TitaniumDarkCard else Color.Transparent)
                        .clickable { selectedTabIsIOwe = false }
                        .padding(vertical = 10.dp)
                        .testTag("tab_por_cobrar"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = null,
                            tint = if (isMeDeben) EmeraldGreen else TitaniumTextSecondary,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Me deben",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isMeDeben) Color.White else TitaniumTextSecondary
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isMeDeben) Color(0xFF2C2C2E) else TitaniumLightBg
                        ) {
                            Text(
                                text = "$countMeDeben",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = if (isMeDeben) EmeraldGreen else TitaniumTextPrimary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Tab: Debo (Por pagar)
                val isDebo = selectedTabIsIOwe
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isDebo) TitaniumDarkCard else Color.Transparent)
                        .clickable { selectedTabIsIOwe = true }
                        .padding(vertical = 10.dp)
                        .testTag("tab_por_pagar"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = null,
                            tint = if (isDebo) CoralRed else TitaniumTextSecondary,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Debo",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isDebo) Color.White else TitaniumTextSecondary
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isDebo) Color(0xFF2C2C2E) else TitaniumLightBg
                        ) {
                            Text(
                                text = "$countDebo",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = if (isDebo) CoralRed else TitaniumTextPrimary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // -------------------------------------------------------------
            // Hero Card: TOTAL POR COBRAR / PAGAR (Titanium Dark)
            // -------------------------------------------------------------
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = TitaniumDarkCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedTabIsIOwe) "TOTAL POR PAGAR" else "TOTAL POR COBRAR",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 0.8.sp,
                            color = Color(0xFF8E8E93)
                        )

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF1E382A)
                        ) {
                            Text(
                                text = "$recoveryPercentage% recuperado",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = EmeraldGreen,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = Formatters.formatMoney(totalPending),
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = Color.White,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = "COP",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = Color(0xFF8E8E93),
                            modifier = Modifier.padding(bottom = 5.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Recaudado: ${Formatters.formatMoney(totalPaid)} COP • Total inicial: ${Formatters.formatMoney(totalOriginal)}",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 11.sp,
                        color = Color(0xFFAEAEB2)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // -------------------------------------------------------------
            // Debts List
            // -------------------------------------------------------------
            if (filteredDebts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (selectedTabIsIOwe) "No tienes deudas por pagar registradas."
                        else "No tienes cobros pendientes registrados.",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 13.sp,
                        color = TitaniumTextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredDebts, key = { it.id }) { debt ->
                        CupertinoDebtCardItem(
                            debt = debt,
                            onCardClick = { selectedDebtForDetail = debt },
                            onPayClick = { selectedDebtForPayment = debt }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(90.dp))
                    }
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
private fun CupertinoDebtCardItem(
    debt: DebtEntity,
    onCardClick: () -> Unit,
    onPayClick: () -> Unit
) {
    val context = LocalContext.current
    val initial = debt.name.trim().take(1).uppercase()
    val isCreditCard = debt.name.contains("tarjeta", ignoreCase = true)
    val progress = if (debt.totalAmount > 0) (debt.paidAmount / debt.totalAmount).toFloat().coerceIn(0f, 1f) else 0f
    val percentInt = (progress * 100).toInt()
    val dateFormat = remember { SimpleDateFormat("d MMM", Locale("es", "CO")) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onCardClick)
            .testTag("debt_card_${debt.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top Row: Avatar, Name & Concept, RESTA amount
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar circle
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (debt.isIOwe) CoralRedLight else EmeraldGreenLight),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCreditCard) {
                        Icon(
                            imageVector = Icons.Default.CreditCard,
                            contentDescription = null,
                            tint = if (debt.isIOwe) CoralRed else EmeraldGreen,
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Text(
                            text = initial,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = if (debt.isIOwe) CoralRed else EmeraldGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = debt.name,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TitaniumTextPrimary
                    )
                    Text(
                        text = if (debt.isIOwe) "Deuda pendiente" else "Préstamo otorgado",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 12.sp,
                        color = TitaniumTextSecondary
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "RESTA",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 0.6.sp,
                        color = TitaniumTextSecondary
                    )
                    Text(
                        text = Formatters.formatMoney(debt.remainingAmount),
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = if (debt.isIOwe) CoralRed else TitaniumTextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar & Percentage
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$percentInt%  Pagado: ${Formatters.formatMoney(debt.paidAmount)} de ${Formatters.formatMoney(debt.totalAmount)}",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 11.sp,
                    color = TitaniumTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (debt.isIOwe) CoralRed else EmeraldGreen,
                trackColor = TitaniumLightBg
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Actions: Status pill + WhatsApp button + Abonar button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status pill
                if (debt.lastPaymentDate != null) {
                    Text(
                        text = "Último: ${dateFormat.format(debt.lastPaymentDate)}",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 11.sp,
                        color = TitaniumTextSecondary
                    )
                } else {
                    Text(
                        text = if (debt.pendingDays > 0) "⏱ Hace ${debt.pendingDays}d" else "Al día",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 11.sp,
                        color = if (debt.pendingDays > 15) CoralRed else TitaniumTextSecondary
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // WhatsApp share button
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                val message = if (debt.isIOwe) {
                                    "Hola ${debt.name}, te escribo respecto al saldo pendiente de ${Formatters.formatMoney(debt.remainingAmount)}."
                                } else {
                                    "Hola ${debt.name}, cordial saludo. Te recuerdo el saldo pendiente de ${Formatters.formatMoney(debt.remainingAmount)} de nuestro préstamo. ¡Gracias!"
                                }
                                val uri = Uri.parse("https://api.whatsapp.com/send?text=${Uri.encode(message)}")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                try {
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            },
                        shape = RoundedCornerShape(10.dp),
                        color = TitaniumLightBg
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Compartir",
                                tint = TitaniumTextPrimary,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "WhatsApp",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp,
                                color = TitaniumTextPrimary
                            )
                        }
                    }

                    // + Abonar button
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable(onClick = onPayClick),
                        shape = RoundedCornerShape(10.dp),
                        color = ElectricBlue
                    ) {
                        Text(
                            text = "+ Abonar",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}
