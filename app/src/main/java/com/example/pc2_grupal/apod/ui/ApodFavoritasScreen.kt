package com.example.pc2_grupal.apod.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pc2_grupal.apod.data.ApodEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApodFavoritasScreen(
    viewModel: ApodViewModel,
    onAbrirDetalle: (ApodEntity) -> Unit,
    onVolver: () -> Unit
) {
    val favoritas by viewModel.favoritas.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis imágenes favoritas") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (favoritas.isEmpty()) {
            MensajeCentrado(
                "Aún no tienes imágenes favoritas. Toca el corazón en una imagen para guardarla aquí.",
                Modifier.padding(padding)
            )
        } else {
            // Tocar el corazón aquí la quita de favoritos y desaparece de la lista
            ListaApod(
                imagenes = favoritas,
                onAbrir = onAbrirDetalle,
                onFavorito = viewModel::cambiarFavorito,
                modifier = Modifier.padding(padding)
            )
        }
    }
}
