package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Formatters {
    private val decimalSymbols = DecimalFormatSymbols(Locale("es", "CO")).apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }
    private val decimalFormat = DecimalFormat("#,##0", decimalSymbols)
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("dd MMM", Locale("es", "CO"))

    fun formatMoney(amount: Double): String {
        return "$ ${decimalFormat.format(amount)}"
    }

    fun formatMoneySigned(amount: Double, isIncome: Boolean): String {
        val sign = if (isIncome) "+$" else "−$"
        return "$sign${decimalFormat.format(amount)}"
    }

    fun formatTime(timestamp: Long): String {
        return timeFormat.format(Date(timestamp))
    }

    fun formatDateGroup(timestamp: Long): String {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply { timeInMillis = timestamp }

        return when {
            isSameDay(now, target) -> "Hoy"
            isYesterday(now, target) -> "Ayer"
            else -> dateFormat.format(Date(timestamp)).uppercase()
        }
    }

    private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    private fun isYesterday(now: Calendar, target: Calendar): Boolean {
        val yesterday = Calendar.getInstance().apply {
            timeInMillis = now.timeInMillis
            add(Calendar.DAY_OF_YEAR, -1)
        }
        return isSameDay(yesterday, target)
    }
}

object CategoryIcons {
    fun getIcon(category: String, concept: String = ""): ImageVector {
        val lowerConcept = concept.lowercase()
        val lowerCat = category.lowercase()

        return when {
            lowerConcept.contains("pantaloneta") || lowerConcept.contains("licra") || lowerConcept.contains("ropa") || lowerCat.contains("ropa") -> Icons.Default.Checkroom
            lowerConcept.contains("envío") || lowerConcept.contains("envio") || lowerCat.contains("envio") || lowerCat.contains("transporte") -> Icons.Default.LocalShipping
            lowerConcept.contains("chuzo") || lowerConcept.contains("comida") || lowerConcept.contains("almuerzo") || lowerConcept.contains("cena") || lowerCat.contains("comida") -> Icons.Default.Restaurant
            lowerConcept.contains("venta") || lowerCat.contains("venta") -> Icons.Default.ShoppingBag
            lowerCat.contains("préstamo") || lowerCat.contains("prestamo") || lowerConcept.contains("préstamo") || lowerConcept.contains("prestamo") -> Icons.Default.Payments
            lowerCat.contains("trabajo") || lowerCat.contains("sueldo") || lowerConcept.contains("trabajo") -> Icons.Default.Work
            lowerCat.contains("servicio") || lowerConcept.contains("luz") || lowerConcept.contains("agua") -> Icons.Default.Bolt
            lowerCat.contains("arriendo") || lowerCat.contains("hogar") -> Icons.Default.Home
            lowerCat.contains("deuda") -> Icons.Default.AccountBalance
            lowerCat.contains("transferencia") -> Icons.Default.SwapHoriz
            lowerCat.contains("salud") || lowerCat.contains("farmacia") -> Icons.Default.MedicalServices
            lowerCat.contains("compras") || lowerCat.contains("supermercado") -> Icons.Default.ShoppingCart
            else -> Icons.Default.AccountBalanceWallet
        }
    }
}
