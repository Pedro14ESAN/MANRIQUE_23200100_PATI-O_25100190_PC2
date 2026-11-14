package com.example.pc2_grupal.asteroides.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.pc2_grupal.ui.navigation.PestanaPrincipal

private const val RUTA_LISTA = "asteroides/lista"
private const val RUTA_DETALLE = "asteroides/detalle/{id}"

/** Navegación interna de la opción Asteroides (Alumno 2). */
fun NavGraphBuilder.asteroidesGraph(navController: NavHostController) {
    navigation(startDestination = RUTA_LISTA, route = PestanaPrincipal.ASTEROIDES.ruta) {
        composable(RUTA_LISTA) { entrada ->
            AsteroidesListaScreen(
                viewModel = viewModelCompartido(entrada, navController),
                onAbrirDetalle = { navController.navigate("asteroides/detalle/${Uri.encode(it.id)}") },
                onAbrirFavoritos = { }
            )
        }
        composable(
            RUTA_DETALLE,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { entrada ->
            AsteroideDetalleScreen(
                id = entrada.arguments?.getString("id").orEmpty(),
                viewModel = viewModelCompartido(entrada, navController),
                onVolver = { navController.popBackStack() }
            )
        }
    }
}

/** Un solo AsteroideViewModel para todas las pantallas de la opción Asteroides. */
@Composable
private fun viewModelCompartido(entrada: NavBackStackEntry, navController: NavHostController): AsteroideViewModel {
    val padre = remember(entrada) { navController.getBackStackEntry(PestanaPrincipal.ASTEROIDES.ruta) }
    return viewModel(padre)
}
