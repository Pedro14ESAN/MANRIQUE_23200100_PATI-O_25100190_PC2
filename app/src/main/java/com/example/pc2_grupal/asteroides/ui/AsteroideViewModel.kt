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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * Un solo ViewModel para la lista, el detalle y "Mis asteroides".
 * Los datos salen siempre de Room; la API solo se usa en [refrescar] para actualizar la base.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AsteroideViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = AsteroideRepository(
        api = RetrofitInstance.crear<NeoWsApi>(),
        dao = AppDatabase.obtener(application).asteroideDao()
    )

    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _soloPeligrosos = MutableStateFlow(false)
    val soloPeligrosos: StateFlow<Boolean> = _soloPeligrosos.asStateFlow()

    private val idSeleccionado = MutableStateFlow<String?>(null)

    /** Según el interruptor se consulta en Room la lista completa o solo los peligrosos. */
    val asteroides: StateFlow<List<AsteroideEntity>> = _soloPeligrosos
        .flatMapLatest { filtro -> repositorio.lista(filtro) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val favoritos: StateFlow<List<AsteroideEntity>> = repositorio.favoritos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Asteroide que se muestra en el detalle, según el id seleccionado. */
    val detalle: StateFlow<AsteroideEntity?> = idSeleccionado
        .flatMapLatest { id -> if (id == null) flowOf(null) else repositorio.asteroide(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    init {
        refrescar()
    }

    fun refrescar() {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            try {
                repositorio.refrescar()
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                _error.value = "Sin conexión a internet: no se pudo consultar la NASA."
            } catch (e: HttpException) {
                _error.value = "La NASA respondió con un error (código ${e.code()})."
            } catch (e: Exception) {
                _error.value = "No se pudo cargar la información de la NASA."
            } finally {
                _cargando.value = false
            }
        }
    }

    fun cambiarFiltro(activo: Boolean) {
        _soloPeligrosos.value = activo
    }

    fun seleccionarAsteroide(id: String) {
        idSeleccionado.value = id
    }

    fun cambiarFavorito(asteroide: AsteroideEntity) {
        viewModelScope.launch { repositorio.cambiarFavorito(asteroide.id, !asteroide.esFavorito) }
    }
}
