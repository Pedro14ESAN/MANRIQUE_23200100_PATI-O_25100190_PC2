package com.example.pc2_grupal.apod.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pc2_grupal.apod.data.ApodApi
import com.example.pc2_grupal.apod.data.ApodEntity
import com.example.pc2_grupal.apod.data.ApodRepository
import com.example.pc2_grupal.data.local.AppDatabase
import com.example.pc2_grupal.data.remote.RetrofitInstance
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ApodUiState(
    val cargando: Boolean = false,
    val imagenes: List<ApodEntity> = emptyList(),
    val error: String? = null
)

class ApodViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = ApodRepository(
        api = RetrofitInstance.crear<ApodApi>(),
        dao = AppDatabase.obtener(application).apodDao()
    )

    private val cargando = MutableStateFlow(false)
    private val error = MutableStateFlow<String?>(null)

    /** Estado de la lista: los datos salen de Room, la carga y el error de la consulta a la API. */
    val uiState: StateFlow<ApodUiState> =
        combine(repositorio.imagenes, cargando, error) { imagenes, cargando, error ->
            ApodUiState(cargando = cargando, imagenes = imagenes, error = error)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ApodUiState(cargando = true))

    val favoritas: StateFlow<List<ApodEntity>> =
        repositorio.favoritas.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

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

    fun imagen(fecha: String): StateFlow<ApodEntity?> =
        repositorio.imagen(fecha).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun cambiarFavorito(imagen: ApodEntity) {
        viewModelScope.launch { repositorio.cambiarFavorito(imagen) }
    }
}
