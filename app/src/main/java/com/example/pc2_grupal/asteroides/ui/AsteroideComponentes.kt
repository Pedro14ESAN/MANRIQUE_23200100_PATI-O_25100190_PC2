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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
            contentDescription = if (esFavorito) "Quitar de Mis asteroides" else "Agregar a Mis asteroides",
            tint = if (esFavorito) MaterialTheme.colorScheme.primary else LocalContentColor.current
        )
    }
}

/** Etiqueta roja que distingue a los asteroides potencialmente peligrosos. */
@Composable
fun EtiquetaPeligroso() {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.error,
        contentColor = MaterialTheme.colorScheme.onError
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(Icons.Filled.Warning, contentDescription = null, modifier = Modifier.size(16.dp))
            Text("Potencialmente peligroso", style = MaterialTheme.typography.labelMedium)
        }
    }
}

/** Tarjeta de un asteroide; los potencialmente peligrosos usan el color errorContainer. */
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
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        } else {
            CardDefaults.cardColors()
        }
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(asteroide.nombre, style = MaterialTheme.typography.titleMedium)
                Text("Aproximación: ${asteroide.fechaAproximacion}", style = MaterialTheme.typography.bodySmall)
                Text("Diámetro máx.: ${conUnidad(asteroide.diametroMaxM, "m")}", style = MaterialTheme.typography.bodySmall)
                Text("Velocidad: ${conUnidad(asteroide.velocidadKmh, "km/h")}", style = MaterialTheme.typography.bodySmall)
                Text("Distancia: ${conUnidad(asteroide.distanciaKm, "km")}", style = MaterialTheme.typography.bodySmall)
                if (asteroide.esPeligroso) {
                    Box(modifier = Modifier.padding(top = 4.dp)) { EtiquetaPeligroso() }
                }
            }
            BotonFavoritoAsteroide(asteroide.esFavorito, onFavorito)
        }
    }
}

/** Lista que usan la pantalla principal y "Mis asteroides". */
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

/** Mensaje centrado que reemplaza a la lista cuando no hay datos o hubo un error. */
@Composable
fun MensajeAsteroides(
    texto: String,
    modifier: Modifier = Modifier,
    accion: (@Composable () -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(texto, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
            accion?.invoke()
        }
    }
}
