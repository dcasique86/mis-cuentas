package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.data.entity.AutoRuleEntity
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
import com.example.ui.theme.TitaniumLightBg
import com.example.ui.theme.TitaniumTextPrimary
import com.example.ui.theme.TitaniumTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoRulesScreen(
    rules: List<AutoRuleEntity>,
    onAddRule: (AutoRuleEntity) -> Unit,
    onUpdateRule: (AutoRuleEntity) -> Unit,
    onDeleteRule: (AutoRuleEntity) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    var ruleToDelete by remember { mutableStateOf<AutoRuleEntity?>(null) }

    val activeCount = rules.count { it.isActive }

    val suggestedPresets = listOf(
        "rappi" to "Comida",
        "starbucks" to "Comida",
        "farmatodo" to "Salud",
        "oxxo" to "Tienda",
        "cine colombia" to "Entretenimiento",
        "gimnasio" to "Salud",
        "gasolina" to "Transporte",
        "quincena" to "Trabajo"
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = TitaniumLightBg,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "AUTOMATIZACIÓN",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 0.8.sp,
                            color = TitaniumTextSecondary
                        )
                        Text(
                            text = "Reglas Automáticas",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = TitaniumTextPrimary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("rules_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = TitaniumTextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TitaniumLightBg)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = ElectricBlue,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("add_rule_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva regla")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Titanium Dark Card Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = TitaniumDarkCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(ElectricBlueLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.ElectricBolt,
                                    contentDescription = null,
                                    tint = ElectricBlue,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "CLASIFICACIÓN EN TIEMPO REAL",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    letterSpacing = 0.8.sp,
                                    color = Color(0xFF8E8E93)
                                )
                                Text(
                                    text = "$activeCount reglas activas",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Al escribir o dictar por voz palabras clave (ej. 'Uber', 'Almuerzo', 'Netflix'), la categoría y el tipo se asignan al instante.",
                            fontFamily = PoppinsFontFamily,
                            fontSize = 12.sp,
                            color = Color(0xFFAEAEB2),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Quick presets
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Sugerencias populares:",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TitaniumTextPrimary
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(suggestedPresets) { (kw, cat) ->
                            val exists = rules.any { it.keyword.equals(kw, ignoreCase = true) }
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (exists) Color(0xFFE5E5EA) else SurfaceWhite,
                                shadowElevation = if (exists) 0.dp else 1.dp,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable(enabled = !exists) {
                                        onAddRule(
                                            AutoRuleEntity(
                                                keyword = kw,
                                                targetCategory = cat,
                                                isIncome = cat == "Trabajo"
                                            )
                                        )
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = if (exists) "✓ $kw" else "+ $kw",
                                        fontFamily = PoppinsFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = if (exists) TitaniumTextSecondary else TitaniumTextPrimary
                                    )
                                    Text(
                                        text = "→ $cat",
                                        fontFamily = PoppinsFontFamily,
                                        fontSize = 11.sp,
                                        color = ElectricBlue
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Rules List
            items(rules, key = { it.id }) { rule ->
                RuleItemCard(
                    rule = rule,
                    onToggleActive = { onUpdateRule(rule.copy(isActive = !rule.isActive)) },
                    onDelete = { ruleToDelete = rule }
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    // Add Dialog
    if (showDialog) {
        AddRuleDialog(
            onDismiss = { showDialog = false },
            onSave = { entity ->
                onAddRule(entity)
                showDialog = false
            }
        )
    }

    // Delete Confirmation
    if (ruleToDelete != null) {
        val target = ruleToDelete!!
        AlertDialog(
            onDismissRequest = { ruleToDelete = null },
            containerColor = SurfaceWhite,
            shape = RoundedCornerShape(24.dp),
            title = {
                Text(
                    text = "Eliminar regla",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = CoralRed
                )
            },
            text = {
                Text(
                    text = "¿Deseas eliminar la regla para '${target.keyword}'?",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 14.sp,
                    color = TitaniumTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteRule(target)
                        ruleToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoralRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Eliminar", color = Color.White, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { ruleToDelete = null }) {
                    Text("Cancelar", color = TitaniumTextSecondary, fontFamily = PoppinsFontFamily)
                }
            }
        )
    }
}

@Composable
fun RuleItemCard(
    rule: AutoRuleEntity,
    onToggleActive: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ElectricBlueLight
                ) {
                    Text(
                        text = "\"${rule.keyword}\"",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = ElectricBlue,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = TitaniumTextSecondary,
                    modifier = Modifier.size(14.dp)
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (rule.isIncome) EmeraldGreenLight else CoralRedLight
                ) {
                    Text(
                        text = rule.targetCategory + if (rule.isIncome) " (Ingreso)" else "",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = if (rule.isIncome) EmeraldGreen else CoralRed,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = rule.isActive,
                    onCheckedChange = { onToggleActive() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = ElectricBlue
                    )
                )
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = TitaniumTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AddRuleDialog(
    onDismiss: () -> Unit,
    onSave: (AutoRuleEntity) -> Unit
) {
    var keyword by remember { mutableStateOf("") }
    var targetCategory by remember { mutableStateOf("Comida") }
    var isIncome by remember { mutableStateOf(false) }

    val categories = listOf(
        "Comida", "Transporte", "Hogar", "Entretenimiento", "Trabajo", "Salud", "Tienda", "Ropa", "Educación"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceWhite,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = "Nueva regla automática",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                color = TitaniumTextPrimary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Si el concepto contiene esta palabra clave:",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 12.sp,
                    color = TitaniumTextSecondary
                )

                OutlinedTextField(
                    value = keyword,
                    onValueChange = { keyword = it },
                    label = { Text("Palabra clave (ej. rappi, gym, claro)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricBlue,
                        focusedLabelColor = ElectricBlue,
                        unfocusedBorderColor = TitaniumBorder
                    )
                )

                // Income / Expense selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            isIncome = false
                            targetCategory = "Comida"
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!isIncome) CoralRed else TitaniumLightBg,
                            contentColor = if (!isIncome) Color.White else TitaniumTextPrimary
                        ),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Gasto", fontSize = 12.sp, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            isIncome = true
                            targetCategory = "Trabajo"
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isIncome) EmeraldGreen else TitaniumLightBg,
                            contentColor = if (isIncome) Color.White else TitaniumTextPrimary
                        ),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Ingreso", fontSize = 12.sp, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold)
                    }
                }

                Text(
                    text = "Asignar automáticamente a la categoría:",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 12.sp,
                    color = TitaniumTextSecondary
                )

                // Category chips
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    categories.chunked(3).forEach { chunk ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            chunk.forEach { cat ->
                                val selected = targetCategory == cat
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (selected) ElectricBlue else TitaniumLightBg,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { targetCategory = cat }
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.padding(vertical = 7.dp)
                                    ) {
                                        Text(
                                            text = cat,
                                            fontFamily = PoppinsFontFamily,
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 11.sp,
                                            color = if (selected) Color.White else TitaniumTextPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (keyword.isNotBlank()) {
                        onSave(
                            AutoRuleEntity(
                                keyword = keyword.trim().lowercase(),
                                targetCategory = targetCategory,
                                isIncome = isIncome,
                                isActive = true
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ElectricBlue,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = keyword.isNotBlank()
            ) {
                Text("Guardar regla", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TitaniumTextSecondary, fontFamily = PoppinsFontFamily)
            }
        }
    )
}
