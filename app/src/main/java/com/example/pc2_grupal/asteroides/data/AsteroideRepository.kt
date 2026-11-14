package com.example.pc2_grupal.asteroides.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Gestiona la API NeoWs y la base local. Las pantallas leen siempre de Room;
 * [actualizar] solo descarga de la API y guarda en la base.
 */
class AsteroideRepository(
    private val api: NeoWsApi,
    private val dao: AsteroideDao
) {

    val asteroides: Flow<List<AsteroideEntity>> = dao.observarTodos()

    val favoritos: Flow<List<AsteroideEntity>> = dao.observarFavoritos()

    fun asteroide(id: String): Flow<AsteroideEntity?> = dao.observarPorId(id)

    /** Descarga el feed de los últimos 7 días y lo guarda conservando los favoritos. */
    suspend fun actualizar() {
        val hoy = LocalDate.now()
        val formato = DateTimeFormatter.ISO_LOCAL_DATE
        val respuesta = api.obtenerFeed(
            fechaInicio = hoy.minusDays(6).format(formato),
            fechaFin = hoy.format(formato)
        )
        val favoritos = dao.idsFavoritos().toSet()
        val entidades = respuesta.todosLosAsteroides().mapNotNull { dto ->
            dto.aEntity(esFavorito = dto.id in favoritos)
        }
        dao.insertarTodos(entidades)
    }

    suspend fun cambiarFavorito(asteroide: AsteroideEntity) {
        dao.actualizarFavorito(asteroide.id, !asteroide.esFavorito)
    }
}
