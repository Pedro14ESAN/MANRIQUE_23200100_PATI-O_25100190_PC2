package com.example.pc2_grupal.apod.data

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * APOD desde el sitio de NASA Science. Devuelve una lista ordenada de la más
 * reciente a la más antigua; con per_page = 7 se obtienen los últimos 7 días.
 * Usa una URL completa porque está en otro dominio que la URL base de Retrofit.
 */
interface ApodApi {

    @GET("https://science.nasa.gov/wp-json/wp/v2/apod-basic")
    suspend fun obtenerImagenes(
        @Query("per_page") cantidad: Int = 7
    ): List<ApodDto>
}
