package com.pajasoft.vetcare.ui.components

import java.text.SimpleDateFormat
import java.util.Locale

fun formatFecha(iso: String?): String {
    if (iso.isNullOrBlank()) return ""
    val patterns = listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy-MM-dd"
    )
    for (p in patterns) {
        try {
            val parser = SimpleDateFormat(p, Locale.getDefault())
            val date = parser.parse(iso) ?: continue
            val out = SimpleDateFormat("dd MMM yyyy", Locale("es", "ES"))
            return out.format(date).replaceFirstChar { it.uppercase() }
        } catch (_: Exception) { /* prueba el siguiente patrón */ }
    }
    return iso.take(10)
}
