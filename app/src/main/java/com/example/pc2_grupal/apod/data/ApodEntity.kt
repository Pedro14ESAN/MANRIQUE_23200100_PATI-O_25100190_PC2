package com.example.pc2_grupal.apod.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/** Tabla de imágenes APOD. La fecha es única por imagen, así que es la clave. */
@Entity(tableName = "apod")
data class ApodEntity(
    @PrimaryKey val fecha: String,
    val titulo: String,
    val explicacion: String,
    val url: String?,
    val hdurl: String?,
    @ColumnInfo(name = "tipo_media") val tipoMedia: String,
    val copyright: String?,
    @ColumnInfo(name = "es_favorito") val esFavorito: Boolean = false
)
