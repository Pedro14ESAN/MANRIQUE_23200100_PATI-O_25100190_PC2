package com.example.pc2_grupal.apod.ui

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

private const val RUTA_LISTA = "imagenes/lista"
private const val RUTA_DETALLE = "imagenes/detalle/{fecha}"
private const val RUTA_FAVORITAS = "imagenes/favoritas"

/** Navegación interna de la opción Imágenes (Alumno 1): lista, detalle y favoritas. */
fun NavGraphBuilder.imagenesGraph(navController: NavHostController) {
    navigation(startDestination = RUTA_LISTA, route = PestanaPrincipal.IMAGENES.ruta) {
        composable(RUTA_LISTA) { entrada ->
            ApodListaScreen(
                viewModel = viewModelCompartido(entrada, navController),
                onAbrirDetalle = { navController.navigate("imagenes/detalle/${Uri.encode(it.fecha)}") },
                onAbrirFavoritas = { navController.navigate(RUTA_FAVORITAS) }
            )
        }
        composable(RUTA_FAVORITAS) { entrada ->
            ApodFavoritasScreen(
                viewModel = viewModelCompartido(entrada, navController),
                onAbrirDetalle = { navController.navigate("imagenes/detalle/${Uri.encode(it.fecha)}") },
                onVolver = { navController.popBackStack() }
            )
        }
        composable(
            RUTA_DETALLE,
            arguments = listOf(navArgument("fecha") { type = NavType.StringType })
        ) { entrada ->
            ApodDetalleScreen(
                fecha = entrada.arguments?.getString("fecha").orEmpty(),
                viewModel = viewModelCompartido(entrada, navController),
                onVolver = { navController.popBackStack() }
            )
        }
    }
}

/** Un solo ApodViewModel para todas las pantallas de la opción Imágenes. */
@Composable
private fun viewModelCompartido(entrada: NavBackStackEntry, navController: NavHostController): ApodViewModel {
    val padre = remember(entrada) { navController.getBackStackEntry(PestanaPrincipal.IMAGENES.ruta) }
    return viewModel(padre)
}
