package com.example

import com.example.data.entity.AccountingType
import com.example.data.entity.DebtEntity
import com.example.data.entity.TransactionEntity
import com.example.ui.components.Formatters
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FinanceLogicTest {

    @Test
    fun testCurrencyFormatting() {
        val money = Formatters.formatMoney(13500.0)
        assertEquals("$ 13.500", money)

        val signedPos = Formatters.formatMoneySigned(140.0, true)
        assertEquals("+$140", signedPos)

        val signedNeg = Formatters.formatMoneySigned(600.0, false)
        assertEquals("−$600", signedNeg)
    }

    @Test
    fun testDebtRemainingCalculation() {
        val debt = DebtEntity(
            name = "Carlos",
            totalAmount = 750000.0,
            paidAmount = 200000.0,
            isIOwe = true
        )
        assertEquals(550000.0, debt.remainingAmount, 0.001)
    }

    @Test
    fun testTransactionTypes() {
        val sale = TransactionEntity(
            type = AccountingType.VENTA.name,
            concept = "Venta pantaloneta",
            category = "Venta",
            paymentMethod = "Efectivo",
            amount = 140.0,
            isIncome = true,
            timestamp = System.currentTimeMillis()
        )
        assertTrue(sale.isIncome)
        assertEquals(140.0, sale.amount, 0.001)
    }
}
