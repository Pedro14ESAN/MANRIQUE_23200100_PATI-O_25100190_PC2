package com.example.pc2_grupal.asteroides.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsteroideDetalleScreen(
    id: String,
    viewModel: AsteroideViewModel,
    onVolver: () -> Unit
) {
    LaunchedEffect(id) { viewModel.seleccionarAsteroide(id) }
    val detalle by viewModel.detalle.collectAsStateWithLifecycle()
    // Mientras llega el seleccionado, detalle podría tener todavía el asteroide anterior
    val asteroide = detalle?.takeIf { it.id == id }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del asteroide") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    asteroide?.let { actual ->
                        BotonFavoritoAsteroide(actual.esFavorito) { viewModel.cambiarFavorito(actual) }
                    }
                }
            )
        }
    ) { padding ->
        if (asteroide == null) {
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(asteroide.nombre, style = MaterialTheme.typography.headlineSmall)
            if (asteroide.esPeligroso) EtiquetaPeligroso()
            HorizontalDivider()
            Dato("Id", asteroide.id)
            Dato("Fecha de aproximación", asteroide.fechaAproximacion)
            Dato("Diámetro mínimo estimado", conUnidad(asteroide.diametroMinM, "m"))
            Dato("Diámetro máximo estimado", conUnidad(asteroide.diametroMaxM, "m"))
            Dato("Velocidad relativa", conUnidad(asteroide.velocidadKmh, "km/h"))
            Dato("Distancia de paso", conUnidad(asteroide.distanciaKm, "km"))
            Dato("Potencialmente peligroso", if (asteroide.esPeligroso) "Sí" else "No")
        }
    }
}

@Composable
private fun Dato(etiqueta: String, valor: String) {
    Column {
        Text(
            etiqueta,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(valor, style = MaterialTheme.typography.bodyLarge)
    }
}
