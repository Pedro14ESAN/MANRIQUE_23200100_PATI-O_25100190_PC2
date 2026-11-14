package com.example.pc2_grupal.apod.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Gestiona la API y la base local. Las pantallas leen siempre de Room;
 * [actualizar] solo descarga de la API y guarda en la base.
 */
class ApodRepository(
    private val api: ApodApi,
    private val dao: ApodDao
) {

    val imagenes: Flow<List<ApodEntity>> = dao.observarTodas()

    val favoritas: Flow<List<ApodEntity>> = dao.observarFavoritas()

    fun imagen(fecha: String): Flow<ApodEntity?> = dao.observarPorFecha(fecha)

    /** Descarga los últimos 7 días (desde hace 6 días hasta hoy) y los guarda conservando los favoritos. */
    suspend fun actualizar() {
        val hoy = LocalDate.now()
        val formato = DateTimeFormatter.ISO_LOCAL_DATE
        val respuesta = api.obtenerImagenes(
            fechaInicio = hoy.minusDays(6).format(formato),
            fechaFin = hoy.format(formato)
        )
        val favoritas = dao.fechasFavoritas().toSet()
        val entidades = respuesta.mapNotNull { dto ->
            dto.aEntity(esFavorito = dto.date in favoritas)
        }
        dao.insertarTodas(entidades)
    }

    suspend fun cambiarFavorito(imagen: ApodEntity) {
        dao.actualizarFavorito(imagen.fecha, !imagen.esFavorito)
    }
}
