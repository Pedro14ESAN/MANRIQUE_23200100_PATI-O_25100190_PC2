package com.example.pc2_grupal.apod.data

import retrofit2.http.GET
import retrofit2.http.Query

/** Endpoint APOD. La api_key la agrega RetrofitInstance. Fechas en formato AAAA-MM-DD. */
interface ApodApi {

    @GET("planetary/apod")
    suspend fun obtenerImagenes(
        @Query("start_date") fechaInicio: String,
        @Query("end_date") fechaFin: String
    ): List<ApodDto>
}
