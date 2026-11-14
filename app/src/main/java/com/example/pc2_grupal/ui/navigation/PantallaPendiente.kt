package com.example.pc2_grupal.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/** Pantalla temporal mientras cada alumno arma su opción. */
@Composable
fun PantallaPendiente(nombre: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("$nombre: en construcción", style = MaterialTheme.typography.titleMedium)
    }
}
