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
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GreenIncome
import com.example.ui.theme.RedExpense
import com.example.ui.theme.SurfaceDark1
import com.example.ui.theme.SurfaceDarkBorder
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.PlusJakartaSansFontFamily
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.AccountEntity
import com.example.data.entity.AccountType
import com.example.data.entity.SavingsGoalEntity
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
import com.example.ui.theme.TitaniumPurple
import com.example.ui.theme.TitaniumPurpleLight
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
    goals: List<SavingsGoalEntity> = emptyList(),
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
    onContributeToGoal: (SavingsGoalEntity, Double) -> Unit = { _, _ -> },
    onAddGoal: (SavingsGoalEntity) -> Unit = {},
    onNavigateToRecurring: () -> Unit = {},
    onNavigateToAutoRules: () -> Unit = {},
    onVoiceInputClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showAddGoalDialog by remember { mutableStateOf(false) }
    var goalForContribution by remember { mutableStateOf<SavingsGoalEntity?>(null) }
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
        }

        Spacer(modifier = Modifier.height(16.dp))

        // -------------------------------------------------------------
        // Hero Card: Disponible (Dark Petróleo & Dorado según imagen)
        // -------------------------------------------------------------
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("balance_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF142028),
                                Color(0xFF1B2523),
                                Color(0xFF2E2718)
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Top Row: "Disponible", Eye Icon, and Round Chevron
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Disponible",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                            IconButton(
                                onClick = onToggleBalanceVisibility,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = if (isBalanceVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Alternar visibilidad del saldo",
                                    tint = GoldLight,
                                    modifier = Modifier.size(17.dp)
                                )
                            }
                        }

                        // Round Chevron Button
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF2E291C),
                            border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.4f)),
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .clickable { onNavigateToAccounts() }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Ver cuentas",
                                    tint = GoldLight,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Giant Money Amount with Currency Code
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (isBalanceVisible) Formatters.formatMoney(availableBalance) else "$ ••••••",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 34.sp,
                            color = TextPrimary,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = "COP",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = GoldLight,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Two Status Pills: Ingresos & Gastos (exact design from image)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // + Ingresos
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFF132F26),
                            border = BorderStroke(1.dp, Color(0xFF1D4A3A))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = GreenIncome,
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowUpward,
                                            contentDescription = null,
                                            tint = Color(0xFF132F26),
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                                Column {
                                    Text(
                                        text = "Ingresos",
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = if (isBalanceVisible) "+${Formatters.formatMoney(todaySummary.income)}" else "+ •••",
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = GreenIncome
                                    )
                                    Text(
                                        text = "Hoy",
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontSize = 10.sp,
                                        color = Color(0xFF86EFAC)
                                    )
                                }
                            }
                        }

                        // - Gastos
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFF331920),
                            border = BorderStroke(1.dp, Color(0xFF4D222D))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = RedExpense,
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDownward,
                                            contentDescription = null,
                                            tint = Color(0xFF331920),
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                                Column {
                                    Text(
                                        text = "Gastos",
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = if (isBalanceVisible) "-${Formatters.formatMoney(todaySummary.expense)}" else "- •••",
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = RedExpense
                                    )
                                    Text(
                                        text = "Hoy",
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontSize = 10.sp,
                                        color = Color(0xFFFCA5A5)
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
                        color = Color(0xFF1E2D36),
                        border = BorderStroke(1.dp, Color(0xFF283F4C))
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
                                    tint = GoldLight,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Cierre Diario de Caja",
                                    fontFamily = PlusJakartaSansFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = TextPrimary
                                )
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // -------------------------------------------------------------
        // Gastos del mes Card (según especificación visual de referencia)
        // -------------------------------------------------------------
        val monthBudgetTotal = (availableBalance + todaySummary.expense).coerceAtLeast(1.0)
        val monthExpenseRatio = (todaySummary.expense / monthBudgetTotal).coerceIn(0.0, 1.0)

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark1),
            border = BorderStroke(1.dp, SurfaceDarkBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Gastos del mes",
                        fontFamily = PlusJakartaSansFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = "${(monthExpenseRatio * 100).toInt()}%",
                        fontFamily = PlusJakartaSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = GoldPrimary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LinearProgressIndicator(
                    progress = { monthExpenseRatio.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = GoldPrimary,
                    trackColor = Color(0xFF24353F)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${Formatters.formatMoney(todaySummary.expense)} de ${Formatters.formatMoney(monthBudgetTotal)}",
                    fontFamily = PlusJakartaSansFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
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

        Spacer(modifier = Modifier.height(20.dp))

        // -------------------------------------------------------------
        // Metas de Ahorro (UI Principal)
        // -------------------------------------------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.clickable { onNavigateToGoals() }
            ) {
                Icon(
                    imageVector = Icons.Default.Savings,
                    contentDescription = null,
                    tint = EmeraldGreen,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Metas de Ahorro",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TitaniumTextPrimary
                )
                if (goals.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = EmeraldGreenLight
                    ) {
                        val completedCount = goals.count { it.isCompleted || (it.targetAmount > 0 && it.savedAmount >= it.targetAmount) }
                        Text(
                            text = "$completedCount/${goals.size} listas",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = EmeraldGreen,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "+ Nueva",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = ElectricBlue,
                    modifier = Modifier.clickable { showAddGoalDialog = true }
                )
                Text(
                    text = "Ver todas >",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = TitaniumTextSecondary,
                    modifier = Modifier.clickable { onNavigateToGoals() }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (goals.isNotEmpty()) {
            val totalSaved = goals.sumOf { it.savedAmount }
            val totalTarget = goals.sumOf { it.targetAmount }
            val overallFraction = if (totalTarget > 0) (totalSaved / totalTarget).coerceIn(0.0, 1.0) else 0.0

            // Overall savings progress banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToGoals() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                border = BorderStroke(1.dp, TitaniumBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ahorro total acumulado",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            color = TitaniumTextSecondary
                        )
                        Text(
                            text = "${(overallFraction * 100).toInt()}%",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = EmeraldGreen
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${Formatters.formatMoney(totalSaved)} de ${Formatters.formatMoney(totalTarget)}",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TitaniumTextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { overallFraction.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = EmeraldGreen,
                        trackColor = Color(0xFFF0F2F5)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Carousel of goals
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                goals.forEach { goal ->
                    HomeGoalCard(
                        goal = goal,
                        onClick = onNavigateToGoals,
                        onContribute = { goalForContribution = goal }
                    )
                }

                // Add new goal card
                Surface(
                    modifier = Modifier
                        .width(130.dp)
                        .height(170.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { showAddGoalDialog = true },
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceWhite,
                    border = BorderStroke(1.dp, TitaniumBorder),
                    shadowElevation = 0.5.dp
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = ElectricBlueLight,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Nueva meta",
                                    tint = ElectricBlue,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "+ Nueva Meta",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = ElectricBlue,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Crear objetivo",
                            fontFamily = PoppinsFontFamily,
                            fontSize = 10.sp,
                            color = TitaniumTextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            // Empty state card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                border = BorderStroke(1.dp, TitaniumBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = CircleShape,
                        color = EmeraldGreenLight,
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Savings,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Empieza a ahorrar para tus metas",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TitaniumTextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Aparta dinero para viajes, fondo de emergencia, compras o proyectos y registra abonos en segundos.",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 12.sp,
                        color = TitaniumTextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = EmeraldGreen,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { showAddGoalDialog = true }
                    ) {
                        Text(
                            text = "+ Crear primera meta",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
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

    if (goalForContribution != null) {
        ContributeDialog(
            goal = goalForContribution!!,
            onDismiss = { goalForContribution = null },
            onConfirm = { amount ->
                onContributeToGoal(goalForContribution!!, amount)
                goalForContribution = null
            }
        )
    }

    if (showAddGoalDialog) {
        GoalDialog(
            goal = null,
            onDismiss = { showAddGoalDialog = false },
            onConfirm = { newGoal ->
                onAddGoal(newGoal)
                showAddGoalDialog = false
            }
        )
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

@Composable
private fun HomeGoalCard(
    goal: SavingsGoalEntity,
    onClick: () -> Unit,
    onContribute: () -> Unit
) {
    val fraction = if (goal.targetAmount > 0) (goal.savedAmount / goal.targetAmount).coerceIn(0.0, 1.0) else 0.0
    val percentage = (fraction * 100).toInt()
    val isCompleted = goal.isCompleted || fraction >= 1.0

    val (icon, iconBg, iconColor) = when (goal.iconName.lowercase()) {
        "flight", "viaje" -> Triple(Icons.Default.Flight, ElectricBlueLight, ElectricBlue)
        "laptop", "pc" -> Triple(Icons.Default.Laptop, TitaniumPurpleLight, TitaniumPurple)
        "shield", "emergencias" -> Triple(Icons.Default.Shield, EmeraldGreenLight, EmeraldGreen)
        "gift", "navidad" -> Triple(Icons.Default.CardGiftcard, CoralRedLight, CoralRed)
        "car", "auto" -> Triple(Icons.Default.DirectionsCar, TitaniumOrangeLight, TitaniumOrange)
        "home", "hogar" -> Triple(Icons.Default.Home, ElectricBlueLight, ElectricBlue)
        else -> Triple(Icons.Default.Savings, EmeraldGreenLight, EmeraldGreen)
    }

    Card(
        modifier = Modifier
            .width(250.dp)
            .height(170.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = BorderStroke(1.dp, TitaniumBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Row: Icon + Title + Status Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(iconBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = goal.name,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TitaniumTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isCompleted) EmeraldGreenLight else ElectricBlueLight
                ) {
                    Text(
                        text = if (isCompleted) "¡Lista! 🎉" else "$percentage%",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = if (isCompleted) EmeraldGreen else ElectricBlue,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Middle: Saved amount & Progress Bar
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = Formatters.formatMoney(goal.savedAmount),
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (isCompleted) EmeraldGreen else TitaniumTextPrimary
                    )
                    Text(
                        text = "de ${Formatters.formatMoney(goal.targetAmount)}",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 11.sp,
                        color = TitaniumTextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { fraction.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (isCompleted) EmeraldGreen else iconColor,
                    trackColor = Color(0xFFF0F2F5)
                )
            }

            // Bottom: Remaining info + Quick "+ Abonar" Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isCompleted) {
                    Text(
                        text = "Meta alcanzada",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        color = EmeraldGreen
                    )
                } else {
                    val remaining = (goal.targetAmount - goal.savedAmount).coerceAtLeast(0.0)
                    Text(
                        text = "Faltan ${Formatters.formatMoney(remaining)}",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 10.5.sp,
                        color = TitaniumTextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = EmeraldGreenLight,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onContribute)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "Abonar",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = EmeraldGreen
                        )
                    }
                }
            }
        }
    }
}
