package com.example.pc2_grupal.asteroides.data

import retrofit2.http.GET
import retrofit2.http.Query

/** Endpoint NeoWs Feed. El rango máximo es de 7 días. La api_key la agrega RetrofitInstance. */
interface NeoWsApi {

    @GET("neo/rest/v1/feed")
    suspend fun obtenerFeed(
        @Query("start_date") fechaInicio: String,
        @Query("end_date") fechaFin: String
    ): FeedResponseDto
}
