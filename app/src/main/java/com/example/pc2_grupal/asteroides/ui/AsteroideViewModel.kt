package com.example.pc2_grupal.asteroides.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pc2_grupal.asteroides.data.AsteroideEntity
import com.example.pc2_grupal.asteroides.data.AsteroideRepository
import com.example.pc2_grupal.asteroides.data.NeoWsApi
import com.example.pc2_grupal.data.local.AppDatabase
import com.example.pc2_grupal.data.remote.RetrofitInstance
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AsteroidesUiState(
    val cargando: Boolean = false,
    val asteroides: List<AsteroideEntity> = emptyList(),
    val soloPeligrosos: Boolean = false,
    val error: String? = null
)

class AsteroideViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = AsteroideRepository(
        api = RetrofitInstance.crear<NeoWsApi>(),
        dao = AppDatabase.obtener(application).asteroideDao()
    )

    private val cargando = MutableStateFlow(false)
    private val error = MutableStateFlow<String?>(null)
    private val soloPeligrosos = MutableStateFlow(false)

    /** Los datos salen de Room; el interruptor deja solo los potencialmente peligrosos. */
    val uiState: StateFlow<AsteroidesUiState> =
        combine(repositorio.asteroides, cargando, soloPeligrosos, error) { lista, cargando, filtro, error ->
            AsteroidesUiState(
                cargando = cargando,
                asteroides = if (filtro) lista.filter { it.esPeligroso } else lista,
                soloPeligrosos = filtro,
                error = error
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AsteroidesUiState(cargando = true))

    val favoritos: StateFlow<List<AsteroideEntity>> =
        repositorio.favoritos.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        actualizar()
    }

    fun actualizar() {
        viewModelScope.launch {
            cargando.value = true
            error.value = null
            try {
                repositorio.actualizar()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                error.value = "No se pudo consultar la NASA: ${e.message ?: "error desconocido"}"
            } finally {
                cargando.value = false
            }
        }
    }

    fun cambiarFiltro(activo: Boolean) {
        soloPeligrosos.value = activo
    }

    fun asteroide(id: String): StateFlow<AsteroideEntity?> =
        repositorio.asteroide(id).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun cambiarFavorito(asteroide: AsteroideEntity) {
        viewModelScope.launch { repositorio.cambiarFavorito(asteroide) }
    }
}
