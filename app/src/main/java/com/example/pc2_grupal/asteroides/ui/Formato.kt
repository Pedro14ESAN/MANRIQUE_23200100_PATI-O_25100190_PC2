package com.example.pc2_grupal.asteroides.ui

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

// Máximo dos decimales (sin ceros sobrantes) y separador de miles: 45678.123 -> "45,678.12"
private val formatoNumero = DecimalFormat("#,##0.##", DecimalFormatSymbols(Locale.US))

/** Número redondeado a máximo dos decimales seguido de su unidad, por ejemplo "120.5 m". */
fun conUnidad(valor: Double, unidad: String): String = "${formatoNumero.format(valor)} $unidad"
