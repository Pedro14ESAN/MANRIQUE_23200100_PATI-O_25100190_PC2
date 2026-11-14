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
    val asteroides by viewModel.asteroides.collectAsStateWithLifecycle()
    val cargando by viewModel.cargando.collectAsStateWithLifecycle()
    val mensajeError by viewModel.error.collectAsStateWithLifecycle()
    val soloPeligrosos by viewModel.soloPeligrosos.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Asteroides cercanos") },
                actions = {
                    IconButton(onClick = viewModel::refrescar) {
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Solo potencialmente peligrosos", modifier = Modifier.weight(1f))
                Switch(checked = soloPeligrosos, onCheckedChange = viewModel::cambiarFiltro)
            }

            when {
                // Primera carga: todavía no hay nada guardado
                cargando && asteroides.isEmpty() -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }

                asteroides.isEmpty() -> {
                    val error = mensajeError
                    val texto = when {
                        error != null && soloPeligrosos ->
                            "$error\nNo hay asteroides potencialmente peligrosos guardados."
                        error != null -> "$error\nTodavía no hay asteroides guardados."
                        soloPeligrosos -> "No hay asteroides potencialmente peligrosos en los últimos 7 días."
                        else -> "No hay asteroides para mostrar en los últimos 7 días."
                    }
                    MensajeAsteroides(texto = texto) {
                        Button(onClick = viewModel::refrescar) { Text("Reintentar") }
                    }
                }

                else -> {
                    if (cargando) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                    // El error se muestra sin ocultar los datos guardados en Room
                    mensajeError?.let {
                        Text(
                            "$it Se muestran los asteroides guardados.",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                    ListaAsteroides(
                        asteroides = asteroides,
                        onAbrir = onAbrirDetalle,
                        onFavorito = viewModel::cambiarFavorito
                    )
                }
            }
        }
    }
}
