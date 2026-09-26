package com.example

import androidx.compose.ui.text.AnnotatedString
import com.example.data.entity.AccountingType
import com.example.data.entity.DebtEntity
import com.example.data.entity.TransactionEntity
import com.example.ui.components.Formatters
import com.example.ui.components.ThousandsSeparatorVisualTransformation
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
    fun testThousandsSeparatorVisualTransformation() {
        val vt = ThousandsSeparatorVisualTransformation()

        assertEquals("5.000", vt.filter(AnnotatedString("5000")).text.text)
        assertEquals("10.000", vt.filter(AnnotatedString("10000")).text.text)
        assertEquals("150.000", vt.filter(AnnotatedString("150000")).text.text)
        assertEquals("15.000.000", vt.filter(AnnotatedString("15000000")).text.text)
        assertEquals("500", vt.filter(AnnotatedString("500")).text.text)
        assertEquals("50", vt.filter(AnnotatedString("50")).text.text)
        assertEquals("5", vt.filter(AnnotatedString("5")).text.text)
        assertEquals("", vt.filter(AnnotatedString("")).text.text)

        // Test offset mapping consistency
        val transformed = vt.filter(AnnotatedString("15000000"))
        // transformed text is "15.000.000" (length 10)
        assertEquals(10, transformed.text.text.length)
        val mapping = transformed.offsetMapping
        // original index 0 -> 0
        assertEquals(0, mapping.originalToTransformed(0))
        // original index 8 (end of 15000000) -> 10 (end of 15.000.000)
        assertEquals(10, mapping.originalToTransformed(8))
        // transformed index 10 -> original index 8
        assertEquals(8, mapping.transformedToOriginal(10))
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
