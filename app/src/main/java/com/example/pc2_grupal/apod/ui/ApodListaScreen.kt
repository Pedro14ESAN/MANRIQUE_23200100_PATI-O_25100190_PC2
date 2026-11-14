package com.example.pc2_grupal.apod.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pc2_grupal.apod.data.ApodEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApodListaScreen(
    viewModel: ApodViewModel,
    onAbrirDetalle: (ApodEntity) -> Unit,
    onAbrirFavoritas: () -> Unit
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Imágenes del día") },
                actions = {
                    IconButton(onClick = viewModel::actualizar) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Actualizar")
                    }
                    IconButton(onClick = onAbrirFavoritas) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Mis imágenes favoritas")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            when {
                // Primera carga: todavía no hay nada guardado
                estado.cargando && estado.imagenes.isEmpty() -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }

                estado.imagenes.isEmpty() -> MensajeCentrado(
                    texto = estado.error ?: "No hay imágenes para mostrar."
                ) {
                    Button(onClick = viewModel::actualizar) { Text("Reintentar") }
                }

                else -> {
                    if (estado.cargando) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                    estado.error?.let {
                        Text(
                            it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                    ListaApod(
                        imagenes = estado.imagenes,
                        onAbrir = onAbrirDetalle,
                        onFavorito = viewModel::cambiarFavorito
                    )
                }
            }
        }
    }
}
