package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MisCuentasLightColorScheme = lightColorScheme(
    primary = ElectricBlue,
    onPrimary = Color.White,
    primaryContainer = ElectricBlueLight,
    onPrimaryContainer = ElectricBlueDark,
    secondary = EmeraldGreen,
    onSecondary = Color.White,
    secondaryContainer = EmeraldGreenLight,
    onSecondaryContainer = EmeraldGreenDark,
    tertiary = CoralRed,
    onTertiary = Color.White,
    tertiaryContainer = CoralRedLight,
    onTertiaryContainer = CoralRed,
    background = TitaniumLightBg,
    onBackground = TitaniumTextPrimary,
    surface = SurfaceWhite,
    onSurface = TitaniumTextPrimary,
    surfaceVariant = TitaniumLightBg,
    onSurfaceVariant = TitaniumTextSecondary,
    outline = TitaniumBorder,
    outlineVariant = TitaniumDivider,
    error = CoralRed,
    onError = Color.White
)

private val MisCuentasDarkColorScheme = darkColorScheme(
    primary = ElectricBlue,
    onPrimary = Color.White,
    primaryContainer = TitaniumDarkElevated,
    onPrimaryContainer = Color.White,
    secondary = EmeraldGreen,
    onSecondary = Color.White,
    background = TitaniumDarkCard,
    onBackground = Color.White,
    surface = TitaniumDarkCard,
    onSurface = Color.White,
    surfaceVariant = TitaniumDarkElevated,
    onSurfaceVariant = TitaniumTextSecondary,
    outline = TitaniumDarkElevated,
    error = CoralRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = MisCuentasLightColorScheme,
        typography = Typography,
        content = content
    )
}
