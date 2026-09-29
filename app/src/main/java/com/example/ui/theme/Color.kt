package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// =================================================================
// Paleta Oficial "Mis Cuentas" - Dark Mode Premium
// Extraída directamente de la especificación de diseño del usuario:
// - #0F2D3A (Petróleo)           -> Fondo principal de la app
// - #1E4A5A (Petróleo claro)     -> Acentos petróleo, bordes y superficies elevadas
// - #8B2F3A (Vino suave)         -> Acento secundario y contenedor de gastos
// - #C79A3A (Dorado principal)   -> Acento primario, marca, tab activa, bordes hero
// - #E8C678 (Dorado claro)       -> Resplandores dorados, gradientes y chips
// - #1A1A1A (Superficie 1)       -> Tarjetas principales
// - #2A2F36 (Superficie 2)       -> Chips, píldoras, filtros, búsqueda
// - #EDEDED (Texto principal)    -> Títulos, saldos, valores destacados
// - #94A3B8 (Texto secundario)   -> Subtítulos, fechas, notas
// - #34D399 (Ingreso / Verde)    -> Montos positivos y badges
// - #F87171 (Gasto / Rojo Coral) -> Montos negativos y deudas
// =================================================================

// 1. Fondos y Bases Petróleo
val PetrolDark = Color(0xFF0F2D3A)         // #0F2D3A Fondo base de pantallas
val PetrolDarkest = Color(0xFF081A22)      // Fondo inferior / BottomBar
val PetrolLight = Color(0xFF1E4A5A)        // #1E4A5A Acento petróleo claro
val PetrolBorder = Color(0xFF1E3F4E)       // Bordes sutiles petróleo

// 2. Dorados Prémium
val GoldPrimary = Color(0xFFC79A3A)        // #C79A3A Dorado principal
val GoldLight = Color(0xFFE8C678)          // #E8C678 Dorado claro brillante
val GoldDark = Color(0xFF9E7728)           // Dorado profundo
val GoldContainer = Color(0xFF2E2413)      // Fondo oscuro con tinte dorado

// 3. Vino suave
val SoftWine = Color(0xFF8B2F3A)           // #8B2F3A Vino suave
val SoftWineBg = Color(0xFF2C151B)         // Fondo oscuro vino

// 4. Superficies Oscuras
val SurfaceDark1 = Color(0xFF1A1A1A)       // #1A1A1A Superficie tarjetas base
val SurfaceDark2 = Color(0xFF2A2F36)       // #2A2F36 Superficie 2 (elevada)
val SurfaceDarkCard = Color(0xFF142028)    // Tarjetas combinadas con petróleo
val SurfaceDarkBorder = Color(0xFF243B47)  // Borde fino de tarjetas
val SurfaceDarkDivider = Color(0xFF182D37) // Divisores de fila

// 5. Textos
val TextPrimary = Color(0xFFEDEDED)        // #EDEDED Texto principal
val TextSecondary = Color(0xFF94A3B8)      // Texto secundario / subtítulos
val TextTertiary = Color(0xFF64748B)       // Muted captions

// 6. Semántica financiera
val GreenIncome = Color(0xFF34D399)        // #34D399 Verde ingreso
val GreenIncomeBg = Color(0xFF123528)      // Contenedor verde oscuro
val RedExpense = Color(0xFFF87171)         // #F87171 Rojo gasto
val RedExpenseBg = Color(0xFF3B1C22)       // Contenedor rojo oscuro

// Mappings & Aliases para compatibilidad inmediata en toda la app:
val TitaniumLightBg = PetrolDark
val SurfaceWhite = SurfaceDark1
val TitaniumDarkCard = SurfaceDark1
val TitaniumDarkElevated = SurfaceDark2
val TitaniumDarkBorder = SurfaceDarkBorder
val TitaniumBorder = SurfaceDarkBorder
val TitaniumDivider = SurfaceDarkDivider

val TitaniumTextPrimary = TextPrimary
val TitaniumTextSecondary = TextSecondary
val TitaniumTextTertiary = TextTertiary

val ElectricBlue = GoldPrimary
val ElectricBlueLight = GoldContainer
val ElectricBlueDark = GoldLight

val EmeraldGreen = GreenIncome
val EmeraldGreenLight = GreenIncomeBg
val EmeraldGreenDark = GreenIncome

val CoralRed = RedExpense
val CoralRedLight = RedExpenseBg

val TitaniumOrange = GoldPrimary
val TitaniumOrangeLight = GoldContainer

val TitaniumPurple = PetrolLight
val TitaniumPurpleLight = Color(0xFF1C313C)

val Cream = PetrolDark
val Sand = PetrolDark
val NeutralGray = SurfaceDarkBorder
val SuccessGreen = GreenIncome
val WarningYellow = GoldPrimary
val ErrorRed = RedExpense

val DeepGreen = GoldPrimary
val SoftGreen = GreenIncome
val Coral = RedExpense
val DeepGreenCard = SurfaceDark1
val DeepGreenAccent = SurfaceDark2
val SoftGreenLight = GreenIncomeBg
val CoralLight = RedExpenseBg
val SandLight = SurfaceDark1

val WarmBeigeCardBg = SurfaceDark1
val WarmBeigeGradientStart = SurfaceDark1
val WarmBeigeGradientEnd = SurfaceDark2
val WarmWaveOrange = GoldPrimary
val WarmDarkGreenBtn = SurfaceDark1
val WarmTerracottaBtn = GoldPrimary
val WarmPeachLight = GoldContainer
val WarmGreenAmount = GreenIncome
val WarmRedAmount = RedExpense
val WarmOrangeIcon = GoldPrimary
val DarkCharcoal = SurfaceDark1
val Mocha = TextSecondary
val Peach = GoldContainer
val GreenIncomeContainer = GreenIncomeBg
val RedExpenseContainer = RedExpenseBg
