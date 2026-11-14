package com.example.pc2_grupal.apod.data

import com.google.gson.annotations.SerializedName

/**
 * Elemento de https://science.nasa.gov/wp-json/wp/v2/apod-basic.
 * Ojo: aquí "url" es la página web; la imagen está en "hdurl".
 * explanation, copyright y credit llegan en HTML.
 * Todos los campos son opcionales para que la app no se caiga si falta alguno.
 */
data class ApodDto(
    @SerializedName("date") val date: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("explanation") val explanation: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("hdurl") val hdurl: String?,
    @SerializedName("media_type") val mediaType: String?,
    @SerializedName("copyright") val copyright: String?,
    @SerializedName("credit") val credit: String?,
    @SerializedName("alt") val alt: String?
)
