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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.DailyClosingEntity
import com.example.ui.components.Formatters
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
import com.example.ui.viewmodel.TodaySummary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DailyClosingScreen(
    todaySummary: TodaySummary,
    expectedCash: Double,
    pastClosings: List<DailyClosingEntity>,
    onSaveClosing: (actualCash: Double, note: String) -> Unit,
    onDeleteClosing: (DailyClosingEntity) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSection by remember { mutableStateOf(0) } // 0: Cierre de hoy, 1: Historial
    var actualCashText by remember { mutableStateOf("") }
    var closingNote by remember { mutableStateOf("") }
    var saveSuccessFeedback by remember { mutableStateOf(false) }

    val actualCash = actualCashText.toDoubleOrNull()
    val difference = if (actualCash != null) actualCash - expectedCash else null

    val todayFormatted = remember {
        SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "CO")).format(Date()).replaceFirstChar { it.uppercase() }
    }
    val dateFormat = remember {
        SimpleDateFormat("d MMM yyyy", Locale("es", "CO"))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TitaniumLightBg)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(14.dp))

        // Top Navigation Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = TitaniumTextPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "CIERRE DE CAJA",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 0.8.sp,
                    color = TitaniumTextSecondary
                )
                Text(
                    text = "Cierre del Día",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = TitaniumTextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cupertino Segmented Selector
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFFE5E5EA)
        ) {
            Row(modifier = Modifier.padding(3.dp)) {
                // Tab 0: Cierre de hoy
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(11.dp))
                        .background(if (selectedSection == 0) SurfaceWhite else Color.Transparent)
                        .clickable { selectedSection = 0 }
                        .padding(vertical = 9.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cierre de Hoy",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = if (selectedSection == 0) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp,
                        color = if (selectedSection == 0) TitaniumTextPrimary else TitaniumTextSecondary
                    )
                }

                // Tab 1: Historial
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(11.dp))
                        .background(if (selectedSection == 1) SurfaceWhite else Color.Transparent)
                        .clickable { selectedSection = 1 }
                        .padding(vertical = 9.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Historial",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = if (selectedSection == 1) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp,
                            color = if (selectedSection == 1) TitaniumTextPrimary else TitaniumTextSecondary
                        )
                        if (pastClosings.isNotEmpty()) {
                            Surface(
                                shape = CircleShape,
                                color = if (selectedSection == 1) ElectricBlueLight else Color(0xFFD1D1D6)
                            ) {
                                Text(
                                    text = pastClosings.size.toString(),
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = if (selectedSection == 1) ElectricBlue else TitaniumTextPrimary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedSection == 0) {
            // TAB 0: CIERRE DE HOY
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Today's System Calculations Card (Titanium Dark Card)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = TitaniumDarkCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = todayFormatted.uppercase(),
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 0.8.sp,
                            color = Color(0xFF8E8E93)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Resumen del Sistema",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Ingresos hoy
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total ventas / ingresos hoy",
                                fontFamily = PoppinsFontFamily,
                                fontSize = 13.sp,
                                color = Color(0xFFAEAEB2)
                            )
                            Text(
                                text = "+${Formatters.formatMoney(todaySummary.income)}",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = EmeraldGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Gastos hoy
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total gastos hoy",
                                fontFamily = PoppinsFontFamily,
                                fontSize = 13.sp,
                                color = Color(0xFFAEAEB2)
                            )
                            Text(
                                text = "−${Formatters.formatMoney(todaySummary.expense)}",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = CoralRed
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = Color(0xFF38383A), thickness = 0.8.dp)
                        Spacer(modifier = Modifier.height(14.dp))

                        // Expected cash
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Efectivo esperado según sistema",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "Saldo en caja al momento",
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = 11.sp,
                                    color = Color(0xFF8E8E93)
                                )
                            }
                            Text(
                                text = Formatters.formatMoney(expectedCash),
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 19.sp,
                                color = ElectricBlue
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Input Actual Cash in Hand
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Dinero Físico Real en Mano",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TitaniumTextPrimary
                        )
                        Text(
                            text = "Cuenta tus billetes y monedas e ingresa el monto total.",
                            fontFamily = PoppinsFontFamily,
                            fontSize = 12.sp,
                            color = TitaniumTextSecondary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = actualCashText,
                            onValueChange = { input ->
                                val clean = input.filter { it.isDigit() || it == '.' }
                                actualCashText = clean
                                saveSuccessFeedback = false
                            },
                            placeholder = {
                                Text(
                                    text = "$ 0",
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = 20.sp,
                                    color = TitaniumTextSecondary
                                )
                            },
                            prefix = {
                                Text(
                                    text = "$ ",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = ElectricBlue
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_actual_cash"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricBlue,
                                unfocusedBorderColor = TitaniumBorder
                            )
                        )

                        // Quick Fill button (Cuadrar automático con lo esperado)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = TitaniumLightBg,
                                modifier = Modifier.clickable {
                                    actualCashText = if (expectedCash % 1.0 == 0.0) {
                                        expectedCash.toInt().toString()
                                    } else {
                                        expectedCash.toString()
                                    }
                                }
                            ) {
                                Text(
                                    text = "Usar esperado ($ ${expectedCash.toInt()})",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp,
                                    color = ElectricBlue,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        // Status Comparison Box
                        if (actualCash != null && difference != null) {
                            Spacer(modifier = Modifier.height(14.dp))
                            val isCuadrado = kotlin.math.abs(difference) < 0.01
                            val isSobrante = difference > 0.01
                            val isFaltante = difference < -0.01

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = when {
                                    isCuadrado -> EmeraldGreenLight
                                    isSobrante -> EmeraldGreenLight
                                    else -> CoralRedLight
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(
                                                when {
                                                    isCuadrado -> EmeraldGreen
                                                    isSobrante -> EmeraldGreen
                                                    else -> CoralRed
                                                }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = when {
                                                isCuadrado -> "✓"
                                                isSobrante -> "+"
                                                else -> "−"
                                            },
                                            fontFamily = PoppinsFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color.White
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(
                                            text = when {
                                                isCuadrado -> "Cierre cuadrado perfecto ✓"
                                                isSobrante -> "Sobrante de dinero: +${Formatters.formatMoney(difference)}"
                                                else -> "Faltante de dinero: −${Formatters.formatMoney(-difference)}"
                                            },
                                            fontFamily = PoppinsFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = if (isFaltante) CoralRed else EmeraldGreen
                                        )
                                        Text(
                                            text = when {
                                                isCuadrado -> "El dinero contado coincide con los registros del sistema."
                                                isSobrante -> "Tienes más efectivo físico que el registrado."
                                                else -> "Tienes menos efectivo físico que el registrado."
                                            },
                                            fontFamily = PoppinsFontFamily,
                                            fontSize = 11.sp,
                                            color = TitaniumTextSecondary
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Optional Note
                        OutlinedTextField(
                            value = closingNote,
                            onValueChange = { closingNote = it },
                            placeholder = {
                                Text(
                                    text = "Nota u observación (ej. Día lluvioso, ventas bajas)",
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = 12.sp,
                                    color = TitaniumTextSecondary
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricBlue,
                                unfocusedBorderColor = TitaniumBorder
                            ),
                            maxLines = 2
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Button: Guardar Cierre Diario
                Button(
                    onClick = {
                        val cash = actualCash ?: expectedCash
                        onSaveClosing(cash, closingNote.trim())
                        saveSuccessFeedback = true
                        actualCashText = ""
                        closingNote = ""
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("save_daily_closing_button")
                ) {
                    Text(
                        text = if (saveSuccessFeedback) "✓ Cierre Guardado Exitosamente" else "Guardar Cierre Diario",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(60.dp))
            }
        } else {
            // TAB 1: HISTORIAL DE CIERRES
            if (pastClosings.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = TitaniumTextSecondary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Aún no hay cierres diarios guardados.",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TitaniumTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Completa tu primer cierre en la pestaña 'Cierre de Hoy'.",
                            fontFamily = PoppinsFontFamily,
                            fontSize = 12.sp,
                            color = TitaniumTextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(pastClosings, key = { it.id }) { closing ->
                        val isCuadrado = closing.status == "CUADRADA"
                        val isSobrante = closing.status == "SOBRANTE"
                        val isFaltante = closing.status == "FALTANTE"

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = dateFormat.format(closing.timestamp),
                                            fontFamily = PoppinsFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = TitaniumTextPrimary
                                        )
                                        Text(
                                            text = "${closing.totalMovements} movimientos registrados",
                                            fontFamily = PoppinsFontFamily,
                                            fontSize = 11.sp,
                                            color = TitaniumTextSecondary
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = when {
                                            isCuadrado -> EmeraldGreenLight
                                            isSobrante -> EmeraldGreenLight
                                            else -> CoralRedLight
                                        }
                                    ) {
                                        Text(
                                            text = when {
                                                isCuadrado -> "Cuadrado ✓"
                                                isSobrante -> "+${Formatters.formatMoney(closing.difference)}"
                                                else -> "−${Formatters.formatMoney(-closing.difference)}"
                                            },
                                            fontFamily = PoppinsFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = if (isFaltante) CoralRed else EmeraldGreen,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                HorizontalDivider(color = TitaniumDivider, thickness = 0.8.dp)
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = "Esperado",
                                            fontFamily = PoppinsFontFamily,
                                            fontSize = 11.sp,
                                            color = TitaniumTextSecondary
                                        )
                                        Text(
                                            text = Formatters.formatMoney(closing.expectedCash),
                                            fontFamily = PoppinsFontFamily,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.sp,
                                            color = TitaniumTextPrimary
                                        )
                                    }

                                    Column {
                                        Text(
                                            text = "Real contado",
                                            fontFamily = PoppinsFontFamily,
                                            fontSize = 11.sp,
                                            color = TitaniumTextSecondary
                                        )
                                        Text(
                                            text = Formatters.formatMoney(closing.actualCash),
                                            fontFamily = PoppinsFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = EmeraldGreen
                                        )
                                    }

                                    Column {
                                        Text(
                                            text = "Diferencia",
                                            fontFamily = PoppinsFontFamily,
                                            fontSize = 11.sp,
                                            color = TitaniumTextSecondary
                                        )
                                        Text(
                                            text = if (closing.difference == 0.0) "$0"
                                            else if (closing.difference > 0) "+${Formatters.formatMoney(closing.difference)}"
                                            else "−${Formatters.formatMoney(-closing.difference)}",
                                            fontFamily = PoppinsFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = if (isFaltante) CoralRed else EmeraldGreen
                                        )
                                    }
                                }

                                if (closing.note.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "“${closing.note}”",
                                        fontFamily = PoppinsFontFamily,
                                        fontSize = 12.sp,
                                        color = TitaniumTextSecondary,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(60.dp))
                    }
                }
            }
        }
    }
}
