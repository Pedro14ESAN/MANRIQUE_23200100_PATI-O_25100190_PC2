package com.example.pc2_grupal.asteroides.data

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Endpoint NeoWs Feed: asteroides que pasan cerca de la Tierra entre dos fechas (AAAA-MM-DD).
 * El rango máximo que acepta la NASA es de 7 días. La api_key la agrega RetrofitInstance.
 */
interface NeoWsApi {

    @GET("neo/rest/v1/feed")
    suspend fun obtenerFeed(
        @Query("start_date") fechaInicio: String,
        @Query("end_date") fechaFin: String
    ): FeedDto
}
