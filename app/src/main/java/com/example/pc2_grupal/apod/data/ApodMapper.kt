package com.example.pc2_grupal.apod.data

/**
 * Conversión explícita DTO -> Entity.
 * Devuelve null si falta la fecha, porque es la clave de la tabla.
 */
fun ApodDto.aEntity(esFavorito: Boolean = false): ApodEntity? {
    val fecha = date?.takeIf { it.isNotBlank() } ?: return null
    return ApodEntity(
        fecha = fecha,
        titulo = title?.takeIf { it.isNotBlank() } ?: "Sin título",
        explicacion = explanation.orEmpty(),
        url = url?.takeIf { it.isNotBlank() },
        hdurl = hdurl?.takeIf { it.isNotBlank() },
        tipoMedia = mediaType ?: "image",
        copyright = copyright?.trim()?.takeIf { it.isNotBlank() },
        esFavorito = esFavorito
    )
}

/** true cuando la API indica que el contenido es un video y no hay imagen que mostrar. */
val ApodEntity.esVideo: Boolean
    get() = tipoMedia.equals("video", ignoreCase = true)
