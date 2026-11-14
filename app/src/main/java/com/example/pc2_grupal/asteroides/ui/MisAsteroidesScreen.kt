package com.example.pc2_grupal.asteroides.ui

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
import com.example.pc2_grupal.asteroides.data.AsteroideEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisAsteroidesScreen(
    viewModel: AsteroideViewModel,
    onAbrirDetalle: (AsteroideEntity) -> Unit,
    onVolver: () -> Unit
) {
    val favoritos by viewModel.favoritos.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis asteroides") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (favoritos.isEmpty()) {
            MensajeAsteroides(
                "Aún no tienes asteroides en Mis asteroides. Toca la estrella de un asteroide para guardarlo aquí.",
                Modifier.padding(padding)
            )
        } else {
            // Tocar la estrella aquí lo quita de favoritos y desaparece de la lista
            ListaAsteroides(
                asteroides = favoritos,
                onAbrir = onAbrirDetalle,
                onFavorito = viewModel::cambiarFavorito,
                modifier = Modifier.padding(padding)
            )
        }
    }
}
