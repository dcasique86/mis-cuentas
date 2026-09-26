package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// =================================================================
// Cupertino Pro Titanium Design System Palette
// Matches the official Cupertino Pro Titanium specification:
// - Primary:   #1C1C1E (Titanium Dark Black / Charcoal)
// - Secondary: #0A84FF (Cupertino Electric Blue / Vibrant Blue)
// - Tertiary:  #30D158 (Cupertino Neon Emerald Green)
// - Neutral:   #8E8E93 (Cupertino Titanium Slate Gray)
// =================================================================

// Core Cupertino Pro Palette
val CupertinoPrimary = Color(0xFF1C1C1E)      // #1C1C1E Primary Dark
val CupertinoSecondary = Color(0xFF0A84FF)    // #0A84FF Secondary Blue
val CupertinoTertiary = Color(0xFF30D158)     // #30D158 Tertiary Green
val CupertinoNeutral = Color(0xFF8E8E93)      // #8E8E93 Neutral Gray

// Semantic Accents
val CupertinoRed = Color(0xFFFF453A)          // #FF453A Red for expenses & alerts
val CupertinoOrange = Color(0xFFFF9F0A)       // #FF9F0A Warning / Attention
val CupertinoPurple = Color(0xFFBF5AF2)       // #BF5AF2 Indigo / Transfers

// Background & Card Surfaces
val TitaniumLightBg = Color(0xFFF2F4F7)       // Clean app background
val SurfaceWhite = Color(0xFFFFFFFF)          // Surface card white
val TitaniumDarkCard = Color(0xFF1C1C1E)      // Deep Titanium Dark card
val TitaniumDarkElevated = Color(0xFF2C2C2E)  // Elevated Dark Surface (#2C2C2E)
val TitaniumDarkBorder = Color(0xFF3A3A3C)    // Dark border hairline (#3A3A3C)
val TitaniumBorder = Color(0xFFE5E7EB)        // Light border hairline
val TitaniumDivider = Color(0xFFECEFF3)       // Row divider

// Typography
val TitaniumTextPrimary = Color(0xFF1C1C1E)   // Main titles & values
val TitaniumTextSecondary = Color(0xFF8E8E93) // Cupertino Neutral Gray (#8E8E93)
val TitaniumTextTertiary = Color(0xFFAEAEB2)  // Subtle captions (#AEAEB2)

// Blue shades
val ElectricBlue = Color(0xFF0A84FF)          // Secondary (#0A84FF)
val ElectricBlueLight = Color(0xFFE5F1FF)     // Soft blue chip background
val ElectricBlueDark = Color(0xFF0066CC)      // Pressed blue

// Green shades
val EmeraldGreen = Color(0xFF30D158)          // Tertiary (#30D158)
val EmeraldGreenLight = Color(0xFFE8F9EE)     // Soft green chip background
val EmeraldGreenDark = Color(0xFF228B22)

// Red shades
val CoralRed = Color(0xFFFF453A)              // Red (#FF453A)
val CoralRedLight = Color(0xFFFFECEB)         // Soft red chip background

// Orange shades
val TitaniumOrange = Color(0xFFFF9F0A)
val TitaniumOrangeLight = Color(0xFFFFF4E5)

// Purple shades
val TitaniumPurple = Color(0xFFBF5AF2)
val TitaniumPurpleLight = Color(0xFFF5E8FF)

// Legacy Aliases for Seamless Backward Compatibility Across Existing Screens
val DeepGreen = ElectricBlue
val SoftGreen = EmeraldGreen
val Coral = CoralRed
val Cream = TitaniumLightBg
val Sand = TitaniumLightBg
val NeutralGray = TitaniumBorder
val TextPrimary = TitaniumTextPrimary
val TextSecondary = TitaniumTextSecondary
val SuccessGreen = EmeraldGreen
val WarningYellow = TitaniumOrange
val ErrorRed = CoralRed

val DeepGreenCard = TitaniumDarkCard
val DeepGreenAccent = TitaniumDarkElevated
val SoftGreenLight = EmeraldGreenLight
val CoralLight = CoralRedLight
val SandLight = SurfaceWhite

val WarmBeigeCardBg = SurfaceWhite
val WarmBeigeGradientStart = SurfaceWhite
val WarmBeigeGradientEnd = SurfaceWhite
val WarmWaveOrange = TitaniumOrange
val WarmDarkGreenBtn = TitaniumDarkCard
val WarmTerracottaBtn = ElectricBlue
val WarmPeachLight = ElectricBlueLight
val WarmGreenAmount = EmeraldGreen
val WarmRedAmount = CoralRed
val WarmOrangeIcon = TitaniumOrange

val DarkCharcoal = TitaniumDarkCard
val Mocha = TitaniumTextSecondary
val Peach = ElectricBlueLight
val GreenIncome = EmeraldGreen
val GreenIncomeContainer = EmeraldGreenLight
val RedExpense = CoralRed
val RedExpenseContainer = CoralRedLight
