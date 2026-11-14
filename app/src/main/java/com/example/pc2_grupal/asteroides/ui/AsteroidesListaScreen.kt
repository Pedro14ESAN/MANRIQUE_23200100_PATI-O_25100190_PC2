package com.example.pc2_grupal.asteroides.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pc2_grupal.asteroides.data.AsteroideEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsteroidesListaScreen(
    viewModel: AsteroideViewModel,
    onAbrirDetalle: (AsteroideEntity) -> Unit,
    onAbrirFavoritos: () -> Unit
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Asteroides cercanos") },
                actions = {
                    IconButton(onClick = viewModel::actualizar) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Actualizar")
                    }
                    IconButton(onClick = onAbrirFavoritos) {
                        Icon(Icons.Filled.Star, contentDescription = "Mis asteroides")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // Interruptor de filtro en la parte superior de la lista
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Solo potencialmente peligrosos", modifier = Modifier.weight(1f))
                Switch(checked = estado.soloPeligrosos, onCheckedChange = viewModel::cambiarFiltro)
            }
            if (estado.cargando && estado.asteroides.isNotEmpty()) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            when {
                estado.cargando && estado.asteroides.isEmpty() -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }

                estado.asteroides.isEmpty() -> MensajeAsteroides(
                    texto = estado.error ?: if (estado.soloPeligrosos) {
                        "No hay asteroides potencialmente peligrosos en estos 7 días."
                    } else {
                        "No hay asteroides para mostrar."
                    }
                ) {
                    if (estado.error != null) {
                        Button(onClick = viewModel::actualizar) { Text("Reintentar") }
                    }
                }

                else -> {
                    estado.error?.let {
                        Text(
                            it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                    ListaAsteroides(
                        asteroides = estado.asteroides,
                        onAbrir = onAbrirDetalle,
                        onFavorito = viewModel::cambiarFavorito
                    )
                }
            }
        }
    }
}
