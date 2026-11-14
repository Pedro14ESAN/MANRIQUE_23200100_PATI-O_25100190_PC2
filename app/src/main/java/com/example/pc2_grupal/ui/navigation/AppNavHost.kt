package com.example.pc2_grupal.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pc2_grupal.apod.ui.imagenesGraph
import com.example.pc2_grupal.asteroides.ui.asteroidesGraph

/** Navegación principal: barra inferior con Imágenes (Alumno 1) y Asteroides (Alumno 2). */
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val entradaActual by navController.currentBackStackEntryAsState()
    val destinoActual = entradaActual?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                PestanaPrincipal.entries.forEach { pestana ->
                    val seleccionada = destinoActual?.hierarchy?.any { it.route == pestana.ruta } == true
                    NavigationBarItem(
                        selected = seleccionada,
                        onClick = {
                            navController.navigate(pestana.ruta) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(pestana.icono, contentDescription = pestana.titulo) },
                        label = { Text(pestana.titulo) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PestanaPrincipal.IMAGENES.ruta,
            modifier = Modifier.padding(innerPadding)
        ) {
            imagenesGraph(navController)
            asteroidesGraph(navController)
        }
    }
}
