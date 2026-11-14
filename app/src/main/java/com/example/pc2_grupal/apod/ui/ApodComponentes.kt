package com.example.pc2_grupal.apod.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.pc2_grupal.apod.data.ApodEntity
import com.example.pc2_grupal.apod.data.esVideo

/** Corazón lleno si es favorita y vacío si no. */
@Composable
fun BotonFavorito(esFavorito: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (esFavorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = if (esFavorito) "Quitar de favoritos" else "Marcar como favorita",
            tint = if (esFavorito) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/** Caja que se muestra en lugar de la imagen cuando es un video o no hay url. */
@Composable
fun SinImagen(texto: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Filled.PlayCircle, contentDescription = null)
            Text(texto, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ApodItem(
    imagen: ApodEntity,
    onClick: () -> Unit,
    onFavorito: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        val modificadorImagen = Modifier
            .fillMaxWidth()
            .height(200.dp)
        when {
            // Si es video no se intenta cargar la imagen
            imagen.esVideo -> SinImagen("Contenido en video", modificadorImagen)
            imagen.url == null -> SinImagen("Imagen no disponible", modificadorImagen)
            else -> AsyncImage(
                model = imagen.url,
                contentDescription = imagen.titulo,
                contentScale = ContentScale.Crop,
                modifier = modificadorImagen
            )
        }
        Row(
            modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    imagen.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(imagen.fecha, style = MaterialTheme.typography.bodySmall)
                if (imagen.esVideo) {
                    Text("Contenido en video", style = MaterialTheme.typography.labelMedium)
                }
            }
            BotonFavorito(imagen.esFavorito, onFavorito)
        }
    }
}

/** Lista reutilizada por la pantalla principal y por "Mis imágenes favoritas". */
@Composable
fun ListaApod(
    imagenes: List<ApodEntity>,
    onAbrir: (ApodEntity) -> Unit,
    onFavorito: (ApodEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(imagenes, key = { it.fecha }) { imagen ->
            ApodItem(
                imagen = imagen,
                onClick = { onAbrir(imagen) },
                onFavorito = { onFavorito(imagen) }
            )
        }
    }
}

@Composable
fun MensajeCentrado(texto: String, modifier: Modifier = Modifier, accion: @Composable (() -> Unit)? = null) {
    Box(modifier = modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(texto, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
            accion?.invoke()
        }
    }
}
