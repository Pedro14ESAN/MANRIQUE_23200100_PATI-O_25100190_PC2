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
private const val RUTA_FAVORITOS = "asteroides/favoritos"

private fun rutaDetalle(id: String) = "asteroides/detalle/${Uri.encode(id)}"

/** Navegación interna de la opción Asteroides (Alumno 2): lista, detalle y "Mis asteroides". */
fun NavGraphBuilder.asteroidesGraph(navController: NavHostController) {
    navigation(startDestination = RUTA_LISTA, route = PestanaPrincipal.ASTEROIDES.ruta) {
        composable(RUTA_LISTA) { entrada ->
            AsteroidesListaScreen(
                viewModel = viewModelCompartido(entrada, navController),
                onAbrirDetalle = { navController.navigate(rutaDetalle(it.id)) },
                onAbrirFavoritos = { navController.navigate(RUTA_FAVORITOS) }
            )
        }
        composable(RUTA_FAVORITOS) { entrada ->
            MisAsteroidesScreen(
                viewModel = viewModelCompartido(entrada, navController),
                onAbrirDetalle = { navController.navigate(rutaDetalle(it.id)) },
                onVolver = { navController.popBackStack() }
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

/**
 * Un solo AsteroideViewModel para las tres pantallas: se guarda en la entrada del grafo
 * "asteroides", así abrir el detalle o "Mis asteroides" no vuelve a consultar la API.
 */
@Composable
private fun viewModelCompartido(entrada: NavBackStackEntry, navController: NavHostController): AsteroideViewModel {
    val padre = remember(entrada) { navController.getBackStackEntry(PestanaPrincipal.ASTEROIDES.ruta) }
    return viewModel(viewModelStoreOwner = padre)
}
