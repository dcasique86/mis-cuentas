package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.Coral
import com.example.ui.theme.Cream
import com.example.ui.theme.DeepGreen
import com.example.ui.theme.PoppinsFontFamily
import com.example.ui.theme.Sand
import com.example.ui.theme.SoftGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun WidgetShowcaseDialog(
    balance: Double,
    todayIncome: Double,
    todayExpense: Double,
    onDismiss: () -> Unit,
    onAddIncomeClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onTransferClick: () -> Unit
) {
    val todayDiff = todayIncome - todayExpense
    val todayDiffText = when {
        todayDiff > 0 -> "↑ + ${Formatters.formatMoney(todayDiff)} hoy"
        todayDiff < 0 -> "↓ - ${Formatters.formatMoney(-todayDiff)} hoy"
        else -> "— $0 hoy"
    }
    val diffColor = when {
        todayDiff > 0 -> Color(0xFF1E7E45)
        todayDiff < 0 -> Color(0xFFD93025)
        else -> TextSecondary
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(24.dp)),
            color = Cream
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Widgets de Inicio",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "Información útil, siempre a la mano.",
                            fontFamily = PoppinsFontFamily,
                            fontSize = 12.sp,
                            color = DeepGreen
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Info banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F3EE))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = DeepGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Para agregarlos: mantén presionada la pantalla de inicio de Android > Widgets > Mis Cuentas.",
                            fontFamily = PoppinsFontFamily,
                            fontSize = 11.sp,
                            color = DeepGreen,
                            lineHeight = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ----------------------------------------------------
                // 1. WIDGET COMPACTO
                // ----------------------------------------------------
                Text(
                    text = "1. Widget Compacto",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextPrimary
                )
                Text(
                    text = "Lo esencial, siempre visible (2x2).",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 11.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Compact Widget Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Sand, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            MisCuentasIcon(size = 26.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Mis Cuentas",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Tu dinero, en orden.",
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = 9.sp,
                                    color = TextSecondary
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Actualizar",
                                tint = TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = Formatters.formatMoney(balance),
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = todayDiffText,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = diffColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ----------------------------------------------------
                // 2. WIDGET ESTÁNDAR
                // ----------------------------------------------------
                Text(
                    text = "2. Widget Estándar",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextPrimary
                )
                Text(
                    text = "Más información, mismo espacio (4x2).",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 11.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Standard Widget Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Sand, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            MisCuentasIcon(size = 28.dp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Mis Cuentas",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Tu dinero, en orden.",
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Actualizar",
                                tint = TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Balance and mini chart
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = Formatters.formatMoney(balance),
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                    color = TextPrimary
                                )
                                Text(
                                    text = todayDiffText,
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = diffColor
                                )
                            }

                            // Decorative activity bars
                            Row(
                                modifier = Modifier.height(30.dp),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Box(modifier = Modifier.size(width = 5.dp, height = 14.dp).background(Color(0xFFA3E0BA), RoundedCornerShape(2.dp)))
                                Spacer(modifier = Modifier.width(3.dp))
                                Box(modifier = Modifier.size(width = 5.dp, height = 22.dp).background(Color(0xFF64C893), RoundedCornerShape(2.dp)))
                                Spacer(modifier = Modifier.width(3.dp))
                                Box(modifier = Modifier.size(width = 5.dp, height = 18.dp).background(Color(0xFF3DA873), RoundedCornerShape(2.dp)))
                                Spacer(modifier = Modifier.width(3.dp))
                                Box(modifier = Modifier.size(width = 5.dp, height = 28.dp).background(Color(0xFF1E7E45), RoundedCornerShape(2.dp)))
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Metric pills
                        Row(modifier = Modifier.fillMaxWidth()) {
                            // Ingresos
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .background(Color(0xFFE6F4EA), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowUpward,
                                        contentDescription = null,
                                        tint = Color(0xFF1E7E45),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(text = "Ingresos", fontFamily = PoppinsFontFamily, fontSize = 10.sp, color = TextSecondary)
                                    Text(
                                        text = Formatters.formatMoney(todayIncome),
                                        fontFamily = PoppinsFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = TextPrimary
                                    )
                                }
                            }

                            // Gastos
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .background(Color(0xFFFCE8E6), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDownward,
                                        contentDescription = null,
                                        tint = Color(0xFFD93025),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(text = "Gastos", fontFamily = PoppinsFontFamily, fontSize = 10.sp, color = TextSecondary)
                                    Text(
                                        text = Formatters.formatMoney(todayExpense),
                                        fontFamily = PoppinsFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick buttons
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = {
                                    onDismiss()
                                    onAddIncomeClick()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SoftGreen)
                            ) {
                                Text(
                                    text = "+ Ingreso",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Button(
                                onClick = {
                                    onDismiss()
                                    onAddExpenseClick()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Coral)
                            ) {
                                Text(
                                    text = "— Gasto",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ----------------------------------------------------
                // 3. WIDGET EXTENDIDO (Dark Theme)
                // ----------------------------------------------------
                Text(
                    text = "3. Widget Extendido",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextPrimary
                )
                Text(
                    text = "Control completo desde tu inicio (4x3 / 4x4).",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 11.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Extended Widget Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF195441), RoundedCornerShape(22.dp)),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0D382C)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            MisCuentasIcon(size = 28.dp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Mis Cuentas",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "Tu dinero, en orden.",
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = 10.sp,
                                    color = Color(0xFF9AC7B6)
                                )
                            }
                            Text(
                                text = "En tiempo real",
                                fontFamily = PoppinsFontFamily,
                                fontSize = 10.sp,
                                color = Color(0xFF85B4A1)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Actualizar",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Two Column Balance & Breakdown
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Left: Balance
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Efectivo disponible",
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = 11.sp,
                                    color = Color(0xFF9AC7B6)
                                )
                                Text(
                                    text = Formatters.formatMoney(balance),
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = todayDiffText,
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Color(0xFF4CE294)
                                )
                            }

                            // Right: Breakdown Box
                            Column(
                                modifier = Modifier
                                    .weight(1.1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF08281E))
                                    .padding(8.dp)
                            ) {
                                // Ingresos
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier.size(16.dp).background(Color(0xFF174E3C), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.ArrowUpward, null, tint = Color(0xFF4CE294), modifier = Modifier.size(10.dp))
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Ingresos", fontFamily = PoppinsFontFamily, fontSize = 10.sp, color = Color(0xFFA5CBBF), modifier = Modifier.weight(1f))
                                    Text(Formatters.formatMoney(todayIncome), fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                // Gastos
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier.size(16.dp).background(Color(0xFF3D1D18), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.ArrowDownward, null, tint = Color(0xFFFF8F6F), modifier = Modifier.size(10.dp))
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Gastos", fontFamily = PoppinsFontFamily, fontSize = 10.sp, color = Color(0xFFA5CBBF), modifier = Modifier.weight(1f))
                                    Text(Formatters.formatMoney(todayExpense), fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                // Balance
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier.size(16.dp).background(Color(0xFF1F4638), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("=", fontFamily = PoppinsFontFamily, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Balance", fontFamily = PoppinsFontFamily, fontSize = 10.sp, color = Color(0xFFA5CBBF), modifier = Modifier.weight(1f))
                                    Text(
                                        text = if (todayDiff >= 0) "+ ${Formatters.formatMoney(todayDiff)}" else "- ${Formatters.formatMoney(-todayDiff)}",
                                        fontFamily = PoppinsFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = Color(0xFF4CE294)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 4 Bottom Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // + Ingreso
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(SoftGreen)
                                    .clickable {
                                        onDismiss()
                                        onAddIncomeClick()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("+", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                                    Text("Ingreso", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 10.sp, color = Color.White)
                                }
                            }

                            // — Gasto
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Coral)
                                    .clickable {
                                        onDismiss()
                                        onAddExpenseClick()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("—", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                                    Text("Gasto", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 10.sp, color = Color.White)
                                }
                            }

                            // Transferir
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF164D3D))
                                    .clickable {
                                        onDismiss()
                                        onTransferClick()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.SwapHoriz, null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    Text("Transferir", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 10.sp, color = Color.White)
                                }
                            }

                            // Más
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF164D3D))
                                    .clickable { onDismiss() },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.GridView, null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    Text("Más", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 10.sp, color = Color.White)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
