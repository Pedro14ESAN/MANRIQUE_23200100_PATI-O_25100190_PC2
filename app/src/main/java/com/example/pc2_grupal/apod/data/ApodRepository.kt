package com.example.pc2_grupal.apod.data

import kotlinx.coroutines.flow.Flow

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

    /** Descarga las 7 imágenes más recientes y las guarda conservando los favoritos. */
    suspend fun actualizar() {
        val respuesta = api.obtenerImagenes(cantidad = 7)
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
