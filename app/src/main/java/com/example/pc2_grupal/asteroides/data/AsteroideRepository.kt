package com.example.pc2_grupal.asteroides.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Gestiona la API NeoWs y la base local. Las pantallas leen siempre de Room;
 * [refrescar] solo descarga de la API y guarda en la base, y Room avisa a la interfaz.
 */
class AsteroideRepository(
    private val api: NeoWsApi,
    private val dao: AsteroideDao
) {

    /** Primer día del rango: 7 días contando hoy (NeoWs no acepta más de 7). */
    private fun inicioDelRango(): String =
        LocalDate.now().minusDays(6).format(DateTimeFormatter.ISO_LOCAL_DATE)

    /** Asteroides de los últimos 7 días guardados en Room; si [soloPeligrosos], solo esos. */
    fun lista(soloPeligrosos: Boolean): Flow<List<AsteroideEntity>> {
        val desde = inicioDelRango()
        return if (soloPeligrosos) dao.observarPeligrosos(desde) else dao.observarLista(desde)
    }

    fun favoritos(): Flow<List<AsteroideEntity>> = dao.observarFavoritos()

    fun asteroide(id: String): Flow<AsteroideEntity?> = dao.observarPorId(id)

    /** Descarga el feed de los últimos 7 días y lo guarda en Room conservando los favoritos. */
    suspend fun refrescar() {
        val respuesta = api.obtenerFeed(
            fechaInicio = inicioDelRango(),
            fechaFin = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        )
        // Se leen los favoritos ANTES del REPLACE; si no, se perderían cada vez que se refresca
        val idsFavoritos = dao.idsFavoritos().toSet()
        dao.insertarTodos(respuesta.aEntities(idsFavoritos))
    }

    suspend fun cambiarFavorito(id: String, esFavorito: Boolean) {
        dao.actualizarFavorito(id, esFavorito)
    }
}
