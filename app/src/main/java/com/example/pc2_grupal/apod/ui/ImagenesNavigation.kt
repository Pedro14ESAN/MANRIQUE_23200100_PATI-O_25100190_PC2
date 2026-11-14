package com.example.pc2_grupal.apod.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.pc2_grupal.ui.navigation.PantallaPendiente
import com.example.pc2_grupal.ui.navigation.PestanaPrincipal

/** Navegación interna de la opción Imágenes (Alumno 1): lista, detalle y favoritas. */
fun NavGraphBuilder.imagenesGraph(navController: NavHostController) {
    navigation(startDestination = "imagenes/lista", route = PestanaPrincipal.IMAGENES.ruta) {
        composable("imagenes/lista") {
            PantallaPendiente("Imágenes (APOD)")
        }
    }
}
