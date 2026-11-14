package com.example.pc2_grupal.asteroides.ui

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

// Máximo dos decimales, con separador de miles
private val formatoNumero = DecimalFormat("#,##0.##", DecimalFormatSymbols(Locale.US))

/** Devuelve el número redondeado con su unidad, o "Sin dato" si la API no lo envió. */
fun conUnidad(valor: Double?, unidad: String): String =
    valor?.let { "${formatoNumero.format(it)} $unidad" } ?: "Sin dato"
