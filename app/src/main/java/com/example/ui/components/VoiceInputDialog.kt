package com.example.ui.components

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.entity.AutoRuleEntity
import com.example.ui.theme.Cream
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.DeepGreen
import com.example.ui.theme.GreenIncome
import com.example.ui.theme.GreenIncomeContainer
import com.example.ui.theme.Mocha
import com.example.ui.theme.Peach
import com.example.ui.theme.PetrolLight
import com.example.ui.theme.PoppinsFontFamily
import com.example.ui.theme.RedExpense
import com.example.ui.theme.RedExpenseContainer
import com.example.ui.theme.Sand
import com.example.ui.theme.SurfaceDark2
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.theme.WarmPeachLight
import com.example.ui.theme.WarmTerracottaBtn
import com.example.util.ParsedVoiceTransaction
import com.example.util.VoiceInputParser
import java.text.NumberFormat
import java.util.Locale

@Composable
fun VoiceInputDialog(
    rules: List<AutoRuleEntity> = emptyList(),
    onDismiss: () -> Unit,
    onConfirm: (ParsedVoiceTransaction, String) -> Unit
) {
    val context = LocalContext.current
    val currencyFormatter = remember {
        NumberFormat.getNumberInstance(Locale.GERMANY).apply {
            minimumFractionDigits = 0
            maximumFractionDigits = 0
        }
    }

    var spokenText by remember { mutableStateOf("") }
    var isListening by remember { mutableStateOf(false) }
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var speechRecognizer by remember { mutableStateOf<SpeechRecognizer?>(null) }
    var paymentMethod by remember { mutableStateOf("Efectivo") }

    val parsed = remember(spokenText, rules) {
        VoiceInputParser.parse(spokenText, rules)
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (granted) {
            // Start listening automatically
            isListening = true
        }
    }

    // Setup speech recognizer
    DisposableEffect(Unit) {
        val isAvailable = SpeechRecognizer.isRecognitionAvailable(context)
        if (isAvailable) {
            val recognizer = SpeechRecognizer.createSpeechRecognizer(context)
            recognizer.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    isListening = true
                }
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {
                    isListening = false
                }
                override fun onError(error: Int) {
                    isListening = false
                }
                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!matches.isNullOrEmpty()) {
                        spokenText = matches[0]
                    }
                    isListening = false
                }
                override fun onPartialResults(partialResults: Bundle?) {
                    val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!matches.isNullOrEmpty()) {
                        spokenText = matches[0]
                    }
                }
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
            speechRecognizer = recognizer
        }

        onDispose {
            speechRecognizer?.destroy()
        }
    }

    fun startListening() {
        if (!hasPermission) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            return
        }
        val recognizer = speechRecognizer
        if (recognizer != null) {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-CO")
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
            }
            try {
                recognizer.startListening(intent)
                isListening = true
            } catch (_: Exception) {
                isListening = false
            }
        }
    }

    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
        } catch (_: Exception) {}
        isListening = false
    }

    // Auto-start listening on open if permission is already granted
    LaunchedEffect(Unit) {
        if (hasPermission) {
            startListening()
        }
    }

    // Pulse animation for listening state
    val infiniteTransition = rememberInfiniteTransition(label = "mic_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isListening) 1.25f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val quickTestPhrases = listOf(
        "Almuerzo 25 mil",
        "Taxi aeropuerto 35000",
        "Mercado éxito 120 mil",
        "Internet claro 85000",
        "Sueldo 1 millón 500 mil",
        "Gasolina 50k"
    )

    AlertDialog(
        onDismissRequest = {
            stopListening()
            onDismiss()
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "🎙️", fontSize = 22.sp)
                    Text(
                        text = "Entrada por voz",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextPrimary
                    )
                }
                IconButton(
                    onClick = {
                        stopListening()
                        onDismiss()
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Mocha)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Mic Button with Animated Ring
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(90.dp)
                ) {
                    if (isListening) {
                        Box(
                            modifier = Modifier
                                .size(84.dp)
                                .scale(pulseScale)
                                .clip(CircleShape)
                                .background(RedExpense.copy(alpha = 0.2f))
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = if (isListening) RedExpense else WarmTerracottaBtn,
                        shadowElevation = 4.dp,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .clickable {
                                if (isListening) stopListening() else startListening()
                            }
                            .testTag("voice_mic_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isListening) Icons.Default.Mic else Icons.Default.MicOff,
                                contentDescription = if (isListening) "Escuchando..." else "Toca para hablar",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }

                Text(
                    text = if (isListening) "Te estoy escuchando... Di algo como 'Almuerzo 25 mil'" else "Toca el micrófono para dictar",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 12.sp,
                    color = if (isListening) RedExpense else Mocha,
                    fontWeight = if (isListening) FontWeight.SemiBold else FontWeight.Normal
                )

                // Spoken / typed text input
                OutlinedTextField(
                    value = spokenText,
                    onValueChange = { spokenText = it },
                    label = { Text("Texto dictado o escrito") },
                    placeholder = { Text("Ej: Taxi al aeropuerto 35 mil") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    textStyle = TextStyle(
                        fontFamily = PoppinsFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = WarmTerracottaBtn,
                        focusedLabelColor = WarmTerracottaBtn,
                        unfocusedBorderColor = PetrolLight,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = SurfaceDark2,
                        unfocusedContainerColor = SurfaceDark2,
                        focusedPlaceholderColor = TextTertiary,
                        unfocusedPlaceholderColor = TextTertiary
                    ),
                    trailingIcon = {
                        if (spokenText.isNotBlank()) {
                            IconButton(onClick = { spokenText = "" }) {
                                Icon(Icons.Default.Refresh, contentDescription = "Limpiar")
                            }
                        }
                    }
                )

                // Quick test suggestions chips
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Probar frases rápidas:",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 11.sp,
                        color = Mocha
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(quickTestPhrases) { phrase ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = WarmPeachLight,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { spokenText = phrase }
                            ) {
                                Text(
                                    text = phrase,
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 11.sp,
                                    color = WarmTerracottaBtn,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }

                // Live Parsed Preview Card
                if (spokenText.isNotBlank()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (parsed.isIncome) GreenIncomeContainer.copy(alpha = 0.5f) else RedExpenseContainer.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (parsed.isIncome) "💰 Ingreso detectado" else "💸 Gasto detectado",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (parsed.isIncome) GreenIncome else RedExpense
                                )
                                Text(
                                    text = "$ ${currencyFormatter.format(parsed.amount)}",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = if (parsed.isIncome) GreenIncome else RedExpense
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Concepto: ${parsed.concept}",
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = 12.sp,
                                    color = DarkCharcoal
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color.White
                                ) {
                                    Text(
                                        text = "⚡ ${parsed.category}",
                                        fontFamily = PoppinsFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.sp,
                                        color = DarkCharcoal,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Payment Method selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Efectivo", "Nequi", "Bancolombia").forEach { method ->
                            val selected = paymentMethod == method
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (selected) DeepGreen else Color(0xFFF3ECE6),
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { paymentMethod = method }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.padding(vertical = 7.dp)
                                ) {
                                    Text(
                                        text = method,
                                        fontFamily = PoppinsFontFamily,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 11.sp,
                                        color = if (selected) Color.White else TextPrimary
                                    )
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
                    stopListening()
                    if (parsed.amount > 0) {
                        onConfirm(parsed, paymentMethod)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = WarmTerracottaBtn,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = parsed.amount > 0,
                modifier = Modifier.testTag("confirm_voice_transaction_button")
            ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Registrar ahora", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    stopListening()
                    onDismiss()
                }
            ) {
                Text("Cancelar", color = TextSecondary, fontFamily = PoppinsFontFamily)
            }
        }
    )
}
