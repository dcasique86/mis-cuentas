package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val MisCuentasLightColorScheme = lightColorScheme(
    primary = DeepGreen,
    onPrimary = Color.White,
    primaryContainer = SoftGreenLight,
    onPrimaryContainer = DeepGreen,
    secondary = SoftGreen,
    onSecondary = DeepGreen,
    secondaryContainer = Sand,
    onSecondaryContainer = TextPrimary,
    tertiary = Coral,
    onTertiary = Color.White,
    tertiaryContainer = CoralLight,
    onTertiaryContainer = TextPrimary,
    background = Cream,
    onBackground = TextPrimary,
    surface = Cream,
    onSurface = TextPrimary,
    surfaceVariant = Sand,
    onSurfaceVariant = TextSecondary,
    outline = NeutralGray,
    error = ErrorRed,
    onError = Color.White
)

private val MisCuentasDarkColorScheme = darkColorScheme(
    primary = SoftGreen,
    onPrimary = DeepGreen,
    primaryContainer = DeepGreenAccent,
    onPrimaryContainer = Color.White,
    secondary = Coral,
    onSecondary = TextPrimary,
    background = Color(0xFF111816),
    onBackground = Color(0xFFF1F5F3),
    surface = Color(0xFF16201D),
    onSurface = Color(0xFFF1F5F3),
    surfaceVariant = Color(0xFF1E2C28),
    onSurfaceVariant = Color(0xFFA5B2AC),
    outline = Color(0xFF2C3E38),
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set false to respect brand identity
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> MisCuentasDarkColorScheme
        else -> MisCuentasLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

