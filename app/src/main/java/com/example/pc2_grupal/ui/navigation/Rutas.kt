package com.example.pc2_grupal.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Public
import androidx.compose.ui.graphics.vector.ImageVector

/** Rutas de las dos pestañas de la barra inferior. Cada una es un grafo con su navegación interna. */
enum class PestanaPrincipal(val ruta: String, val titulo: String, val icono: ImageVector) {
    IMAGENES("imagenes", "Imágenes", Icons.Filled.Image),
    ASTEROIDES("asteroides", "Asteroides", Icons.Filled.Public)
}
