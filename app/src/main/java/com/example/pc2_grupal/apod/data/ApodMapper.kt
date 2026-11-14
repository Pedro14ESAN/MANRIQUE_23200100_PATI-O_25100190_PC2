package com.example.pc2_grupal.apod.data

import androidx.core.text.HtmlCompat

/**
 * Conversión explícita DTO -> Entity.
 * Devuelve null si falta la fecha, porque es la clave de la tabla.
 */
fun ApodDto.aEntity(esFavorito: Boolean = false): ApodEntity? {
    val fecha = date?.takeIf { it.isNotBlank() } ?: return null
    // En este endpoint la imagen está en hdurl; url es la página web
    val imagen = hdurl?.takeIf { it.isNotBlank() }
    return ApodEntity(
        fecha = fecha,
        titulo = title?.takeIf { it.isNotBlank() } ?: "Sin título",
        explicacion = limpiarExplicacion(explanation).ifBlank { alt.orEmpty() },
        url = imagen,
        hdurl = imagen,
        tipoMedia = mediaType ?: "image",
        copyright = textoPlano(copyright).ifBlank { textoPlano(credit) }.takeIf { it.isNotBlank() },
        esFavorito = esFavorito
    )
}

/** Quita las etiquetas HTML y deja solo el texto. */
private fun textoPlano(html: String?): String =
    if (html.isNullOrBlank()) "" else HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim()

/** Quita el "Explanation:" del inicio y el "Tomorrow's picture: ..." del final. */
private fun limpiarExplicacion(html: String?): String =
    textoPlano(html)
        .removePrefix("Explanation:")
        .substringBefore("Tomorrow's picture:")
        .replace(Regex("\\s+"), " ")
        .trim()

/** true cuando la API indica que el contenido es un video y no hay imagen que mostrar. */
val ApodEntity.esVideo: Boolean
    get() = tipoMedia.equals("video", ignoreCase = true)
