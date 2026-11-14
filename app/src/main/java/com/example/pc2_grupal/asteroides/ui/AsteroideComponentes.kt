package com.example.pc2_grupal.asteroides.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pc2_grupal.asteroides.data.AsteroideEntity

/** Estrella llena si es favorito y vacía si no. */
@Composable
fun BotonFavoritoAsteroide(esFavorito: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (esFavorito) Icons.Filled.Star else Icons.Filled.StarBorder,
            contentDescription = if (esFavorito) "Quitar de favoritos" else "Marcar como favorito",
            tint = if (esFavorito) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/** Etiqueta roja para los potencialmente peligrosos. */
@Composable
fun EtiquetaPeligroso() {
    AssistChip(
        onClick = {},
        label = { Text("Potencialmente peligroso") },
        leadingIcon = { Icon(Icons.Filled.Warning, contentDescription = null) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            labelColor = MaterialTheme.colorScheme.onErrorContainer,
            leadingIconContentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    )
}

@Composable
fun AsteroideItem(
    asteroide: AsteroideEntity,
    onClick: () -> Unit,
    onFavorito: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = if (asteroide.esPeligroso) {
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f))
        } else {
            CardDefaults.cardColors()
        }
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(asteroide.nombre, style = MaterialTheme.typography.titleMedium)
                Text("Aproximación: ${asteroide.fechaAproximacion}", style = MaterialTheme.typography.bodySmall)
                Text("Diámetro máx.: ${conUnidad(asteroide.diametroMaxM, "m")}", style = MaterialTheme.typography.bodySmall)
                Text("Velocidad: ${conUnidad(asteroide.velocidadKmH, "km/h")}", style = MaterialTheme.typography.bodySmall)
                Text("Distancia: ${conUnidad(asteroide.distanciaKm, "km")}", style = MaterialTheme.typography.bodySmall)
                if (asteroide.esPeligroso) EtiquetaPeligroso()
            }
            BotonFavoritoAsteroide(asteroide.esFavorito, onFavorito)
        }
    }
}

/** Lista reutilizada por la pantalla principal y por "Mis asteroides". */
@Composable
fun ListaAsteroides(
    asteroides: List<AsteroideEntity>,
    onAbrir: (AsteroideEntity) -> Unit,
    onFavorito: (AsteroideEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(asteroides, key = { it.id }) { asteroide ->
            AsteroideItem(
                asteroide = asteroide,
                onClick = { onAbrir(asteroide) },
                onFavorito = { onFavorito(asteroide) }
            )
        }
    }
}

@Composable
fun MensajeAsteroides(texto: String, modifier: Modifier = Modifier, accion: @Composable (() -> Unit)? = null) {
    Box(modifier = modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(texto, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
            accion?.invoke()
        }
    }
}
