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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CategoryIcons
import com.example.ui.components.Formatters
import com.example.ui.theme.CoralRed
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.PoppinsFontFamily
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TitaniumBorder
import com.example.ui.theme.TitaniumDarkCard
import com.example.ui.theme.TitaniumDivider
import com.example.ui.theme.TitaniumLightBg
import com.example.ui.theme.TitaniumTextPrimary
import com.example.ui.theme.TitaniumTextSecondary
import com.example.ui.viewmodel.MonthlyReportData
import kotlin.math.max

@Composable
fun ReportsScreen(
    report: MonthlyReportData,
    monthOffset: Int,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onResetCurrentMonth: () -> Unit,
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TitaniumLightBg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(14.dp))

        // Screen Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBackClick != null) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(SurfaceWhite, CircleShape)
                        .testTag("reports_back_button")
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
                    text = "REPORTES",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 0.8.sp,
                    color = TitaniumTextSecondary
                )
                Text(
                    text = "Resumen Mensual",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = TitaniumTextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Month Selector Bar (< Septiembre 2026 >)
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceWhite,
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 0.5.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNextMonth) { // +1 offset goes back in past
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Mes anterior",
                        tint = TitaniumDarkCard
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.clickable { onResetCurrentMonth() }
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = ElectricBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = report.monthName,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TitaniumTextPrimary
                    )
                    if (monthOffset != 0) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = TitaniumLightBg
                        ) {
                            Text(
                                text = "Hoy",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = ElectricBlue,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onPreviousMonth,
                    enabled = monthOffset > 0
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Mes siguiente",
                        tint = if (monthOffset > 0) TitaniumDarkCard else Color(0xFFC7C7CC)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // -------------------------------------------------------------
        // Main Net Balance Card (Titanium Dark Card)
        // -------------------------------------------------------------
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = TitaniumDarkCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "BALANCE NETO DEL MES",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 0.8.sp,
                    color = Color(0xFF8E8E93)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (report.balance >= 0) "+${Formatters.formatMoney(report.balance)}"
                    else "-${Formatters.formatMoney(-report.balance)}",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Total Ingresos",
                            fontFamily = PoppinsFontFamily,
                            fontSize = 11.sp,
                            color = Color(0xFF8E8E93)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "+${Formatters.formatMoney(report.totalIncome)}",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = EmeraldGreen
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Total Gastos",
                            fontFamily = PoppinsFontFamily,
                            fontSize = 11.sp,
                            color = Color(0xFF8E8E93)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "-${Formatters.formatMoney(report.totalExpense)}",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = CoralRed
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // -------------------------------------------------------------
        // Month Highlights / Daily Averages
        // -------------------------------------------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Promedio de gasto diario
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Gasto promedio",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 11.sp,
                        color = TitaniumTextSecondary
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = Formatters.formatMoney(report.dailyAverageExpense),
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TitaniumTextPrimary
                    )
                    Text(
                        text = "por día",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 10.sp,
                        color = TitaniumTextSecondary
                    )
                }
            }

            // Día de mayor venta / ingreso
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Mayor ingreso",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 11.sp,
                        color = TitaniumTextSecondary
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = report.highestIncomeDay,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = EmeraldGreen,
                        maxLines = 1
                    )
                    Text(
                        text = "pico del mes",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 10.sp,
                        color = TitaniumTextSecondary
                    )
                }
            }

            // Día de mayor gasto
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Mayor gasto",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 11.sp,
                        color = TitaniumTextSecondary
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = report.highestExpenseDay,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = CoralRed,
                        maxLines = 1
                    )
                    Text(
                        text = "pico de salida",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 10.sp,
                        color = TitaniumTextSecondary
                    )
                }
            }
        }

        // Comparativa con el mes anterior
        if (report.incomeChangePercent != null || report.expenseChangePercent != null) {
            Spacer(modifier = Modifier.height(14.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Comparativa con el mes anterior",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TitaniumTextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    report.expenseChangePercent?.let { change ->
                        val isLess = change <= 0
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (isLess) Icons.Default.TrendingDown else Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = if (isLess) EmeraldGreen else CoralRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (isLess) "Gastaste ${kotlin.math.abs(change).toInt()}% menos que el mes anterior"
                                else "Gastaste ${change.toInt()}% más que el mes anterior",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                color = if (isLess) EmeraldGreen else CoralRed
                            )
                        }
                    }

                    if (report.incomeChangePercent != null && report.expenseChangePercent != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    report.incomeChangePercent?.let { change ->
                        val isMore = change >= 0
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (isMore) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                                contentDescription = null,
                                tint = if (isMore) EmeraldGreen else CoralRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (isMore) "Ingresaste ${change.toInt()}% más que el mes anterior"
                                else "Ingresaste ${kotlin.math.abs(change).toInt()}% menos que el mes anterior",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                color = if (isMore) EmeraldGreen else CoralRed
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // -------------------------------------------------------------
        // Gráfica de barras semanal del mes
        // -------------------------------------------------------------
        Text(
            text = "Evolución por semanas",
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = TitaniumTextPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(ElectricBlue)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Ingresos",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 11.sp,
                        color = TitaniumTextSecondary
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(CoralRed)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Gastos",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 11.sp,
                        color = TitaniumTextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                val maxVal = report.weeklyBreakdown.maxOfOrNull { max(it.income, it.expense) } ?: 1.0
                val safeMax = if (maxVal <= 0.0) 1.0 else maxVal

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    report.weeklyBreakdown.forEach { week ->
                        val incRatio = (week.income / safeMax).toFloat().coerceIn(0.06f, 1f)
                        val expRatio = (week.expense / safeMax).toFloat().coerceIn(0.06f, 1f)

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier.width(60.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.Bottom,
                                modifier = Modifier.height(90.dp)
                            ) {
                                // Income bar (Electric Blue)
                                Box(
                                    modifier = Modifier
                                        .width(16.dp)
                                        .height((90 * incRatio).dp)
                                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                        .background(ElectricBlue)
                                )
                                // Expense bar (Coral Red)
                                Box(
                                    modifier = Modifier
                                        .width(16.dp)
                                        .height((90 * expRatio).dp)
                                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                        .background(CoralRed)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = week.weekLabel,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 11.sp,
                                color = TitaniumTextPrimary
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // -------------------------------------------------------------
        // Categorías con mayor gasto
        // -------------------------------------------------------------
        Text(
            text = "Categorías con mayor gasto",
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = TitaniumTextPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (report.expenseByCategory.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay gastos registrados en este mes.",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 13.sp,
                        color = TitaniumTextSecondary
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
                Column(modifier = Modifier.padding(18.dp)) {
                    report.expenseByCategory.take(5).forEachIndexed { idx, (cat, amount) ->
                        val percent = if (report.totalExpense > 0) (amount / report.totalExpense).toFloat() else 0f
                        val percentFormatted = (percent * 100).toInt()

                        Column(modifier = Modifier.padding(vertical = 6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (idx == 0) CoralRed.copy(alpha = 0.12f) else TitaniumLightBg,
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = (idx + 1).toString(),
                                                fontFamily = PoppinsFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = if (idx == 0) CoralRed else TitaniumTextSecondary
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        imageVector = CategoryIcons.getIcon(cat),
                                        contentDescription = cat,
                                        tint = TitaniumTextPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = cat,
                                        fontFamily = PoppinsFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 14.sp,
                                        color = TitaniumTextPrimary
                                    )
                                }
                                Text(
                                    text = "${Formatters.formatMoney(amount)}  ($percentFormatted%)",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = TitaniumTextPrimary
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { percent },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = if (idx == 0) CoralRed else CoralRed.copy(alpha = 0.65f),
                                trackColor = TitaniumLightBg
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}
