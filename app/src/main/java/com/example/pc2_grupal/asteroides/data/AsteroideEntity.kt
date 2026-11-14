package com.example.pc2_grupal.asteroides.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla de asteroides cercanos (NeoWs). Los números ya se guardan convertidos a Double
 * y en las unidades que muestra la app: metros, km/h y km.
 */
@Entity(tableName = "asteroides")
data class AsteroideEntity(
    @PrimaryKey val id: String,
    val nombre: String,
    // Formato AAAA-MM-DD, así el orden de texto coincide con el orden de fechas
    @ColumnInfo(name = "fecha_aproximacion") val fechaAproximacion: String,
    @ColumnInfo(name = "diametro_min_m") val diametroMinM: Double,
    @ColumnInfo(name = "diametro_max_m") val diametroMaxM: Double,
    @ColumnInfo(name = "velocidad_kmh") val velocidadKmh: Double,
    @ColumnInfo(name = "distancia_km") val distanciaKm: Double,
    @ColumnInfo(name = "es_peligroso") val esPeligroso: Boolean,
    @ColumnInfo(name = "es_favorito") val esFavorito: Boolean = false
)
