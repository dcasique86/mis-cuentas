package com.example.util

import com.example.data.entity.AutoRuleEntity
import java.util.Locale

data class ParsedVoiceTransaction(
    val rawText: String,
    val concept: String,
    val amount: Double,
    val isIncome: Boolean,
    val category: String
)

object VoiceInputParser {

    private val defaultRules = listOf(
        AutoRuleEntity(keyword = "uber", targetCategory = "Transporte"),
        AutoRuleEntity(keyword = "didi", targetCategory = "Transporte"),
        AutoRuleEntity(keyword = "cabify", targetCategory = "Transporte"),
        AutoRuleEntity(keyword = "taxi", targetCategory = "Transporte"),
        AutoRuleEntity(keyword = "gasolina", targetCategory = "Transporte"),
        AutoRuleEntity(keyword = "peaje", targetCategory = "Transporte"),
        AutoRuleEntity(keyword = "transmilenio", targetCategory = "Transporte"),
        AutoRuleEntity(keyword = "bus", targetCategory = "Transporte"),
        AutoRuleEntity(keyword = "metro", targetCategory = "Transporte"),
        AutoRuleEntity(keyword = "parqueadero", targetCategory = "Transporte"),

        AutoRuleEntity(keyword = "almuerzo", targetCategory = "Comida"),
        AutoRuleEntity(keyword = "desayuno", targetCategory = "Comida"),
        AutoRuleEntity(keyword = "cena", targetCategory = "Comida"),
        AutoRuleEntity(keyword = "comida", targetCategory = "Comida"),
        AutoRuleEntity(keyword = "restaurante", targetCategory = "Comida"),
        AutoRuleEntity(keyword = "café", targetCategory = "Comida"),
        AutoRuleEntity(keyword = "cafe", targetCategory = "Comida"),
        AutoRuleEntity(keyword = "mercado", targetCategory = "Comida"),
        AutoRuleEntity(keyword = "éxito", targetCategory = "Comida"),
        AutoRuleEntity(keyword = "exito", targetCategory = "Comida"),
        AutoRuleEntity(keyword = "jumbo", targetCategory = "Comida"),
        AutoRuleEntity(keyword = "d1", targetCategory = "Comida"),
        AutoRuleEntity(keyword = "ara", targetCategory = "Comida"),
        AutoRuleEntity(keyword = "carulla", targetCategory = "Comida"),
        AutoRuleEntity(keyword = "panadería", targetCategory = "Comida"),
        AutoRuleEntity(keyword = "hamburguesa", targetCategory = "Comida"),
        AutoRuleEntity(keyword = "pizza", targetCategory = "Comida"),

        AutoRuleEntity(keyword = "netflix", targetCategory = "Entretenimiento"),
        AutoRuleEntity(keyword = "spotify", targetCategory = "Entretenimiento"),
        AutoRuleEntity(keyword = "cine", targetCategory = "Entretenimiento"),
        AutoRuleEntity(keyword = "película", targetCategory = "Entretenimiento"),
        AutoRuleEntity(keyword = "disney", targetCategory = "Entretenimiento"),
        AutoRuleEntity(keyword = "hbo", targetCategory = "Entretenimiento"),
        AutoRuleEntity(keyword = "prime", targetCategory = "Entretenimiento"),
        AutoRuleEntity(keyword = "juego", targetCategory = "Entretenimiento"),

        AutoRuleEntity(keyword = "arriendo", targetCategory = "Hogar"),
        AutoRuleEntity(keyword = "alquiler", targetCategory = "Hogar"),
        AutoRuleEntity(keyword = "luz", targetCategory = "Hogar"),
        AutoRuleEntity(keyword = "agua", targetCategory = "Hogar"),
        AutoRuleEntity(keyword = "gas", targetCategory = "Hogar"),
        AutoRuleEntity(keyword = "internet", targetCategory = "Hogar"),
        AutoRuleEntity(keyword = "claro", targetCategory = "Hogar"),
        AutoRuleEntity(keyword = "tigo", targetCategory = "Hogar"),
        AutoRuleEntity(keyword = "movistar", targetCategory = "Hogar"),
        AutoRuleEntity(keyword = "aseo", targetCategory = "Hogar"),

        AutoRuleEntity(keyword = "salario", targetCategory = "Trabajo", isIncome = true),
        AutoRuleEntity(keyword = "sueldo", targetCategory = "Trabajo", isIncome = true),
        AutoRuleEntity(keyword = "nómina", targetCategory = "Trabajo", isIncome = true),
        AutoRuleEntity(keyword = "nomina", targetCategory = "Trabajo", isIncome = true),
        AutoRuleEntity(keyword = "honorarios", targetCategory = "Trabajo", isIncome = true),
        AutoRuleEntity(keyword = "venta", targetCategory = "Trabajo", isIncome = true),
        AutoRuleEntity(keyword = "cliente", targetCategory = "Trabajo", isIncome = true),
        AutoRuleEntity(keyword = "freelance", targetCategory = "Trabajo", isIncome = true),

        AutoRuleEntity(keyword = "farmacia", targetCategory = "Salud"),
        AutoRuleEntity(keyword = "droguería", targetCategory = "Salud"),
        AutoRuleEntity(keyword = "medicamento", targetCategory = "Salud"),
        AutoRuleEntity(keyword = "médico", targetCategory = "Salud"),
        AutoRuleEntity(keyword = "doctor", targetCategory = "Salud"),
        AutoRuleEntity(keyword = "cita", targetCategory = "Salud"),

        AutoRuleEntity(keyword = "ropa", targetCategory = "Ropa"),
        AutoRuleEntity(keyword = "zapatos", targetCategory = "Ropa"),
        AutoRuleEntity(keyword = "camisa", targetCategory = "Ropa")
    )

    fun getDefaultRules(): List<AutoRuleEntity> = defaultRules

    fun parse(rawText: String, customRules: List<AutoRuleEntity> = emptyList()): ParsedVoiceTransaction {
        val clean = rawText.trim()
        if (clean.isBlank()) {
            return ParsedVoiceTransaction(
                rawText = "",
                concept = "Transacción rápida",
                amount = 0.0,
                isIncome = false,
                category = "Otros"
            )
        }

        val lower = clean.lowercase(Locale.getDefault())

        // 1. Detect if it's an Income or Expense
        val incomeKeywords = listOf("ingreso", "cobro", "me pagaron", "sueldo", "salario", "nómina", "nomina", "recibí", "gané", "venta")
        val explicitIncome = incomeKeywords.any { lower.contains(it) }

        // 2. Extract Amount
        var detectedAmount = extractAmount(lower)

        // 3. Extract Concept by removing amount tokens and fillers
        var concept = extractConcept(clean)
        if (concept.isBlank()) {
            concept = clean
        }

        // 4. Determine Category using custom rules first, then default rules
        val allRules = (customRules.filter { it.isActive } + defaultRules)
        var matchedCategory: String? = null
        var matchedIncome: Boolean? = null

        val conceptLower = concept.lowercase(Locale.getDefault())
        for (rule in allRules) {
            val kw = rule.keyword.lowercase(Locale.getDefault()).trim()
            if (kw.isNotBlank() && (conceptLower.contains(kw) || lower.contains(kw))) {
                matchedCategory = rule.targetCategory
                if (rule.isIncome) {
                    matchedIncome = true
                }
                break
            }
        }

        val finalCategory = matchedCategory ?: if (explicitIncome || matchedIncome == true) "Trabajo" else "Otros"
        val finalIsIncome = explicitIncome || (matchedIncome == true)

        // Capitalize first letter of concept
        val finalConcept = concept.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

        return ParsedVoiceTransaction(
            rawText = clean,
            concept = finalConcept,
            amount = detectedAmount,
            isIncome = finalIsIncome,
            category = finalCategory
        )
    }

    private fun extractAmount(text: String): Double {
        // Look for patterns like "1.5 millones", "2 millones", "500 mil", "25 mil", "25000", "$30.000", "50k"
        
        // Pattern 1: X millones
        val millonesRegex = Regex("""(\d+(?:[.,]\d+)?)\s*(?:millones|millón|millon)""", RegexOption.IGNORE_CASE)
        val millonesMatch = millonesRegex.find(text)
        if (millonesMatch != null) {
            val numStr = millonesMatch.groupValues[1].replace(',', '.')
            val num = numStr.toDoubleOrNull() ?: 1.0
            return num * 1_000_000.0
        }

        // Pattern 2: "medio millón" or "medio millon"
        if (text.contains("medio millón") || text.contains("medio millon")) {
            return 500_000.0
        }

        // Pattern 3: X mil or Xk
        val milRegex = Regex("""(\d+(?:[.,]\d+)?)\s*(?:mil|k)\b""", RegexOption.IGNORE_CASE)
        val milMatch = milRegex.find(text)
        if (milMatch != null) {
            val numStr = milMatch.groupValues[1].replace(',', '.')
            val num = numStr.toDoubleOrNull() ?: 1.0
            return num * 1_000.0
        }

        // Pattern 4: Direct numbers (e.g. 25000, 25.000, $50000)
        val directNumberRegex = Regex("""(?:\$?\s*)(\d{1,3}(?:[.,]\d{3})+|\d+)""")
        val matches = directNumberRegex.findAll(text).toList()
        if (matches.isNotEmpty()) {
            // Pick the largest number found or the last number
            val numbers = matches.mapNotNull { m ->
                val rawDigits = m.groupValues[1].replace(".", "").replace(",", "")
                rawDigits.toDoubleOrNull()
            }
            if (numbers.isNotEmpty()) {
                return numbers.maxOrNull() ?: 0.0
            }
        }

        return 0.0
    }

    private fun extractConcept(text: String): String {
        // Remove common fillers: "gaste", "gasté", "pagué", "compre", "compré", "ingreso de", "registro", "anota"
        // Also remove amounts: "25 mil", "50k", "100000", "$ 25.000"
        var clean = text
            .replace(Regex("""(?i)\b(gast[eé]|pagu[eé]|compr[eé]|anot[aá]|registr[aá]|ingres[oó]|cobr[eé]|recib[ií])\b"""), "")
            .replace(Regex("""(?i)\b(de|por|un|una|el|la|los|las|en|unos)\b"""), " ")
            .replace(Regex("""(\d+(?:[.,]\d+)?)\s*(?:millones|millón|millon)""", RegexOption.IGNORE_CASE), "")
            .replace(Regex("""(\d+(?:[.,]\d+)?)\s*(?:mil|k)\b""", RegexOption.IGNORE_CASE), "")
            .replace(Regex("""(?:\$?\s*)(\d{1,3}(?:[.,]\d{3})+|\d+)"""), "")
            .replace(Regex("""\s+"""), " ")
            .trim()

        if (clean.length <= 1) {
            // If stripping leaves empty string, fallback to original text without numbers
            clean = text.replace(Regex("""\d+"""), "").replace(Regex("""\s+"""), " ").trim()
        }
        return clean
    }

    fun matchCategory(concept: String, rules: List<AutoRuleEntity>): AutoRuleEntity? {
        val lower = concept.lowercase(Locale.getDefault()).trim()
        if (lower.isBlank()) return null

        val allRules = rules.filter { it.isActive } + defaultRules
        return allRules.firstOrNull { rule ->
            val kw = rule.keyword.lowercase(Locale.getDefault()).trim()
            kw.isNotBlank() && lower.contains(kw)
        }
    }
}
