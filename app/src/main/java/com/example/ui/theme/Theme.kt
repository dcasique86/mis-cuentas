package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// =================================================================
// Esquema de Colores Dark Mode Exclusivo "Mis Cuentas"
// =================================================================
private val MisCuentasDarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = Color(0xFF14191E),
    primaryContainer = GoldContainer,
    onPrimaryContainer = GoldLight,
    secondary = PetrolLight,
    onSecondary = TextPrimary,
    secondaryContainer = SurfaceDark2,
    onSecondaryContainer = TextPrimary,
    tertiary = GreenIncome,
    onTertiary = Color(0xFF0F1E17),
    tertiaryContainer = GreenIncomeBg,
    onTertiaryContainer = GreenIncome,
    background = PetrolDark,
    onBackground = TextPrimary,
    surface = SurfaceDark1,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceDark2,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceDarkBorder,
    outlineVariant = SurfaceDarkDivider,
    error = RedExpense,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Siempre modo oscuro
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = MisCuentasDarkColorScheme,
        typography = Typography,
        content = content
    )
}
