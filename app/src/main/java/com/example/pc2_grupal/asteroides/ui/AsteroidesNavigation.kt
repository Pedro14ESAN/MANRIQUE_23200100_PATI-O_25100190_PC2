package com.example.pc2_grupal.asteroides.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.pc2_grupal.ui.navigation.PantallaPendiente
import com.example.pc2_grupal.ui.navigation.PestanaPrincipal

/**
 * Navegación interna de la opción Asteroides (Alumno 2).
 * Reemplazar PantallaPendiente por la lista y agregar aquí el detalle y "Mis asteroides".
 */
fun NavGraphBuilder.asteroidesGraph(navController: NavHostController) {
    navigation(startDestination = "asteroides/lista", route = PestanaPrincipal.ASTEROIDES.ruta) {
        composable("asteroides/lista") {
            PantallaPendiente("Asteroides (NeoWs)")
        }
    }
}
