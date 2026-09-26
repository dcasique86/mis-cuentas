package com.example.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Visual transformation that displays thousands separators using Colombian convention (dot '.')
 * while preserving clean numeric representation in the underlying TextField value.
 *
 * Example:
 *  "5000"     -> "5.000"
 *  "10000"    -> "10.000"
 *  "150000"   -> "150.000"
 *  "15000000" -> "15.000.000"
 */
class ThousandsSeparatorVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text
        if (raw.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val formatted = StringBuilder()
        val rawToTransformed = IntArray(raw.length + 1)
        val transformedToRaw = ArrayList<Int>()

        for (i in 0 until raw.length) {
            rawToTransformed[i] = formatted.length
            formatted.append(raw[i])
            transformedToRaw.add(i)

            val digitsRemaining = raw.length - (i + 1)
            if (digitsRemaining > 0 && digitsRemaining % 3 == 0) {
                formatted.append('.')
                transformedToRaw.add(i + 1)
            }
        }
        rawToTransformed[raw.length] = formatted.length
        transformedToRaw.add(raw.length)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val clamped = offset.coerceIn(0, raw.length)
                return rawToTransformed[clamped]
            }

            override fun transformedToOriginal(offset: Int): Int {
                val clamped = offset.coerceIn(0, transformedToRaw.size - 1)
                return transformedToRaw[clamped]
            }
        }

        return TransformedText(AnnotatedString(formatted.toString()), offsetMapping)
    }
}
