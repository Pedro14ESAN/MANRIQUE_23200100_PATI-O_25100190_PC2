package com.example.pc2_grupal.apod.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.pc2_grupal.apod.data.esVideo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApodDetalleScreen(
    fecha: String,
    viewModel: ApodViewModel,
    onVolver: () -> Unit
) {
    val imagenFlow = remember(fecha) { viewModel.imagen(fecha) }
    val imagen by imagenFlow.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    imagen?.let { actual ->
                        BotonFavorito(actual.esFavorito) { viewModel.cambiarFavorito(actual) }
                    }
                }
            )
        }
    ) { padding ->
        val actual = imagen
        if (actual == null) {
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Imagen en grande: hdurl si existe y, si no, url
            val urlGrande = actual.hdurl ?: actual.url
            when {
                actual.esVideo -> SinImagen("Contenido en video", Modifier.fillMaxWidth().height(220.dp))
                urlGrande == null -> SinImagen("Imagen no disponible", Modifier.fillMaxWidth().height(220.dp))
                else -> AsyncImage(
                    model = urlGrande,
                    contentDescription = actual.titulo,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(actual.titulo, style = MaterialTheme.typography.headlineSmall)
                Text(actual.fecha, style = MaterialTheme.typography.bodyMedium)
                actual.copyright?.let {
                    Text("Autor: $it", style = MaterialTheme.typography.bodyMedium)
                }
                if (actual.esVideo && actual.url != null) {
                    Text("Video: ${actual.url}", style = MaterialTheme.typography.bodySmall)
                }
                Text(
                    actual.explicacion.ifBlank { "Sin explicación disponible." },
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
