package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.AccountEntity
import com.example.data.entity.AccountType
import com.example.data.entity.TransactionEntity
import com.example.ui.components.Formatters
import com.example.ui.components.MisCuentasHeaderLogo
import com.example.ui.components.TransactionItemRow
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
import com.example.ui.theme.TitaniumDarkElevated
import com.example.ui.theme.TitaniumDivider
import com.example.ui.theme.TitaniumLightBg
import com.example.ui.theme.TitaniumOrange
import com.example.ui.theme.TitaniumOrangeLight
import com.example.ui.theme.TitaniumTextPrimary
import com.example.ui.theme.TitaniumTextSecondary
import com.example.ui.viewmodel.TodaySummary

@Composable
fun HomeScreen(
    availableBalance: Double,
    isBalanceVisible: Boolean,
    onToggleBalanceVisibility: () -> Unit,
    todaySummary: TodaySummary,
    recentTransactions: List<TransactionEntity>,
    accounts: List<AccountEntity> = emptyList(),
    onAddIncomeClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onTransferClick: () -> Unit,
    onMoreActionsClick: () -> Unit,
    onViewAllTransactionsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onTransactionClick: (TransactionEntity) -> Unit,
    totalMeDeben: Double = 0.0,
    countMeDeben: Int = 0,
    totalDebo: Double = 0.0,
    countDebo: Int = 0,
    onNavigateToDebts: (isIOwe: Boolean) -> Unit = {},
    onDailyClosingClick: () -> Unit = {},
    onNavigateToAccounts: () -> Unit = {},
    onNavigateToBudgets: () -> Unit = {},
    onNavigateToGoals: () -> Unit = {},
    onNavigateToRecurring: () -> Unit = {},
    onNavigateToAutoRules: () -> Unit = {},
    onVoiceInputClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TitaniumLightBg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // -------------------------------------------------------------
        // Top Header: Brand on left, Voice & Profile on right
        // -------------------------------------------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MisCuentasHeaderLogo(
                iconSize = 38.dp,
                showTagline = true
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Quick Voice Input Pill (Cupertino style)
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = ElectricBlueLight,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onVoiceInputClick() }
                        .testTag("home_voice_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Entrada por voz",
                            tint = ElectricBlue,
                            modifier = Modifier.size(17.dp)
                        )
                        Text(
                            text = "Voz",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = ElectricBlue
                        )
                    }
                }

                // Profile Avatar Button
                Surface(
                    shape = CircleShape,
                    color = SurfaceWhite,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable { onMoreActionsClick() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil y configuración",
                            tint = TitaniumTextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // -------------------------------------------------------------
        // Hero Card: DINERO DISPONIBLE (Cupertino Titanium Pro)
        // -------------------------------------------------------------
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("balance_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = TitaniumDarkCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Top Row: Label & Eye Icon
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
                            imageVector = Icons.Default.Wallet,
                            contentDescription = null,
                            tint = Color(0xFF8E8E93),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "DINERO DISPONIBLE",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 0.8.sp,
                            color = Color(0xFF8E8E93)
                        )
                    }

                    IconButton(
                        onClick = onToggleBalanceVisibility,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (isBalanceVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Alternar visibilidad del saldo",
                            tint = Color(0xFFAEAEB2),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Giant Money Amount with Currency Code
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = if (isBalanceVisible) Formatters.formatMoney(availableBalance) else "$ ••••••",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        color = Color.White,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "COP",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Color(0xFF8E8E93),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Two Status Pills: Ingresos hoy & Gastos hoy
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // + Ingresos hoy
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF1E382A)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Column {
                                Text(
                                    text = "Ingresos hoy",
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = 10.sp,
                                    color = Color(0xFFA1DDB3)
                                )
                                Text(
                                    text = if (isBalanceVisible) "+${Formatters.formatMoney(todaySummary.income)}" else "+ •••",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = EmeraldGreen
                                )
                            }
                        }
                    }

                    // - Gastos hoy
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF381F23)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = null,
                                tint = CoralRed,
                                modifier = Modifier.size(14.dp)
                            )
                            Column {
                                Text(
                                    text = "Gastos hoy",
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = 10.sp,
                                    color = Color(0xFFFFAEB2)
                                )
                                Text(
                                    text = if (isBalanceVisible) "-${Formatters.formatMoney(todaySummary.expense)}" else "- •••",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = CoralRed
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Action: Cierre Diario de Caja button strip
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onDailyClosingClick() },
                    shape = RoundedCornerShape(14.dp),
                    color = TitaniumDarkElevated
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = TitaniumOrange,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Cierre Diario de Caja",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color(0xFF8E8E93),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // -------------------------------------------------------------
        // Acciones Rápidas (4 Circle Action Buttons)
        // -------------------------------------------------------------
        Text(
            text = "Acciones rápidas",
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = TitaniumTextPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 1. + Ingreso
            HomeQuickActionButton(
                label = "+ Ingreso",
                iconColor = EmeraldGreen,
                containerColor = EmeraldGreenLight,
                onClick = onAddIncomeClick,
                testTag = "add_income_button"
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(22.dp))
            }

            // 2. - Gasto
            HomeQuickActionButton(
                label = "- Gasto",
                iconColor = CoralRed,
                containerColor = CoralRedLight,
                onClick = onAddExpenseClick,
                testTag = "add_expense_button"
            ) {
                Icon(Icons.Default.Remove, contentDescription = null, tint = CoralRed, modifier = Modifier.size(22.dp))
            }

            // 3. Transferir
            HomeQuickActionButton(
                label = "Transferir",
                iconColor = ElectricBlue,
                containerColor = ElectricBlueLight,
                onClick = onTransferClick,
                testTag = "transfer_button"
            ) {
                Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(22.dp))
            }

            // 4. Cierre
            HomeQuickActionButton(
                label = "Cierre",
                iconColor = TitaniumOrange,
                containerColor = TitaniumOrangeLight,
                onClick = onDailyClosingClick,
                testTag = "daily_closing_button"
            ) {
                Icon(Icons.Default.Lock, contentDescription = null, tint = TitaniumOrange, modifier = Modifier.size(20.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // -------------------------------------------------------------
        // Estado de Deudas (Cupertino Split Card)
        // -------------------------------------------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Estado de Deudas",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = TitaniumTextPrimary
            )
            Text(
                text = "Ver todas >",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = ElectricBlue,
                modifier = Modifier.clickable { onNavigateToDebts(false) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Column: Me deben
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToDebts(false) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Me deben",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = TitaniumTextPrimary
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = EmeraldGreenLight
                        ) {
                            Text(
                                text = "$countMeDeben pers.",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = EmeraldGreen,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = Formatters.formatMoney(totalMeDeben),
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = TitaniumTextPrimary
                    )
                    Text(
                        text = "Por cobrar",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 11.sp,
                        color = TitaniumTextSecondary
                    )
                }

                // Vertical Divider
                Box(
                    modifier = Modifier
                        .height(44.dp)
                        .width(1.dp)
                        .background(TitaniumDivider)
                )

                // Right Column: Debo
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 14.dp)
                        .clickable { onNavigateToDebts(true) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Debo",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = TitaniumTextPrimary
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CoralRedLight
                        ) {
                            Text(
                                text = "$countDebo pers.",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = CoralRed,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = Formatters.formatMoney(totalDebo),
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = TitaniumTextPrimary
                    )
                    Text(
                        text = "Por pagar",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 11.sp,
                        color = TitaniumTextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // -------------------------------------------------------------
        // Cuentas y Tarjetas (Horizontal Carousel)
        // -------------------------------------------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.clickable { onNavigateToAccounts() }
            ) {
                Text(
                    text = "Cuentas y Tarjetas",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TitaniumTextPrimary
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.clickable { onNavigateToAccounts() }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
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

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (accounts.isNotEmpty()) {
                accounts.forEach { acc ->
                    val (icon, tag) = when (acc.type) {
                        AccountType.CASH.name -> Pair(Icons.Default.Payments, "Físico")
                        AccountType.WALLET.name -> Pair(Icons.Default.PhoneAndroid, "Billetera")
                        AccountType.BANK.name -> Pair(Icons.Default.AccountBalance, "Banco")
                        AccountType.CREDIT_CARD.name -> Pair(Icons.Default.CreditCard, "Tarjeta")
                        AccountType.SAVINGS.name -> Pair(Icons.Default.Savings, "Ahorro")
                        else -> Pair(Icons.Default.Payments, "Cuenta")
                    }
                    HomeAccountCard(
                        icon = icon,
                        tag = tag,
                        name = acc.name,
                        amount = acc.currentBalance,
                        onClick = onNavigateToAccounts
                    )
                }
            } else {
                // Default fallback accounts if none exist
                HomeAccountCard(
                    icon = Icons.Default.Payments,
                    tag = "Físico",
                    name = "Efectivo",
                    amount = availableBalance,
                    onClick = onNavigateToAccounts
                )
                HomeAccountCard(
                    icon = Icons.Default.PhoneAndroid,
                    tag = "Billetera",
                    name = "Nequi",
                    amount = 0.0,
                    onClick = onNavigateToAccounts
                )
                HomeAccountCard(
                    icon = Icons.Default.AccountBalance,
                    tag = "Banco",
                    name = "Bancolombia",
                    amount = 0.0,
                    onClick = onNavigateToAccounts
                )
            }

            // Quick card to manage or add accounts
            Surface(
                modifier = Modifier
                    .width(115.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .clickable(onClick = onNavigateToAccounts),
                shape = RoundedCornerShape(18.dp),
                color = SurfaceWhite,
                border = BorderStroke(1.dp, TitaniumBorder),
                shadowElevation = 0.5.dp
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = ElectricBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Editar",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = ElectricBlue
                    )
                    Text(
                        text = "Cuentas",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 10.sp,
                        color = TitaniumTextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // -------------------------------------------------------------
        // Movimientos Recientes
        // -------------------------------------------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Movimientos Recientes",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TitaniumTextPrimary
            )
            Text(
                text = "Historial completo",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = ElectricBlue,
                modifier = Modifier.clickable { onViewAllTransactionsClick() }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (recentTransactions.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = SurfaceWhite
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp, horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay movimientos registrados hoy.\nUsa + Ingreso o - Gasto para comenzar.",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 13.sp,
                        color = TitaniumTextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
                    recentTransactions.take(5).forEachIndexed { index, tx ->
                        TransactionItemRow(
                            transaction = tx,
                            onClick = { onTransactionClick(tx) }
                        )
                        if (index < recentTransactions.take(5).size - 1) {
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

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
private fun HomeQuickActionButton(
    label: String,
    iconColor: Color,
    containerColor: Color,
    onClick: () -> Unit,
    testTag: String,
    icon: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Surface(
            modifier = Modifier.size(56.dp),
            shape = CircleShape,
            color = containerColor,
            shadowElevation = 0.5.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                icon()
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            color = TitaniumTextPrimary
        )
    }
}

@Composable
private fun HomeAccountCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tag: String,
    name: String,
    amount: Double,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(150.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = SurfaceWhite,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TitaniumTextPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = tag,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 10.sp,
                    color = TitaniumTextSecondary
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = name,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = TitaniumTextSecondary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = Formatters.formatMoney(amount),
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = TitaniumTextPrimary
            )
        }
    }
}
