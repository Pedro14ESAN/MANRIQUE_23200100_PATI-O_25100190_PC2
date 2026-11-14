package com.example.pc2_grupal.apod.data

import com.google.gson.annotations.SerializedName

/**
 * Respuesta de planetary/apod. Todos los campos son opcionales porque la API
 * puede no enviarlos (por ejemplo hdurl y copyright), y así la app no se cae.
 */
data class ApodDto(
    @SerializedName("date") val date: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("explanation") val explanation: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("hdurl") val hdurl: String?,
    @SerializedName("media_type") val mediaType: String?,
    @SerializedName("copyright") val copyright: String?
)
