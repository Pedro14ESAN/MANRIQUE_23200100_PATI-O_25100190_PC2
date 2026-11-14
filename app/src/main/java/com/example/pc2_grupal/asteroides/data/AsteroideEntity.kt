package com.example.pc2_grupal.asteroides.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/** Tabla de asteroides. Los números ya se guardan convertidos de texto a Double. */
@Entity(tableName = "asteroides")
data class AsteroideEntity(
    @PrimaryKey val id: String,
    val nombre: String,
    @ColumnInfo(name = "fecha_aproximacion") val fechaAproximacion: String,
    @ColumnInfo(name = "epoch_aproximacion") val epochAproximacion: Long?,
    @ColumnInfo(name = "diametro_min_m") val diametroMinM: Double?,
    @ColumnInfo(name = "diametro_max_m") val diametroMaxM: Double?,
    @ColumnInfo(name = "velocidad_km_h") val velocidadKmH: Double?,
    @ColumnInfo(name = "distancia_km") val distanciaKm: Double?,
    @ColumnInfo(name = "es_peligroso") val esPeligroso: Boolean,
    @ColumnInfo(name = "es_favorito") val esFavorito: Boolean = false
)
