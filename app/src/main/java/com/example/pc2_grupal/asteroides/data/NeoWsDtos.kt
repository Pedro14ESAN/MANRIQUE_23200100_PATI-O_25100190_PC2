package com.example.pc2_grupal.asteroides.data

import com.google.gson.annotations.SerializedName

/**
 * Respuesta de neo/rest/v1/feed.
 * near_earth_objects es un objeto cuyas claves son fechas (AAAA-MM-DD) y cuyos valores
 * son listas de asteroides. Todo es nullable para que la app no se cierre si falta un campo;
 * también se aceptan listas nulas o elementos nulos dentro de las listas.
 */
data class FeedDto(
    @SerializedName("element_count") val cantidad: Int?,
    @SerializedName("near_earth_objects") val asteroidesPorFecha: Map<String, List<NeoDto?>?>?
)

data class NeoDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val nombre: String?,
    @SerializedName("is_potentially_hazardous_asteroid") val esPeligroso: Boolean?,
    @SerializedName("estimated_diameter") val diametroEstimado: DiametroEstimadoDto?,
    @SerializedName("close_approach_data") val aproximaciones: List<AproximacionDto?>?
)

data class DiametroEstimadoDto(
    @SerializedName("meters") val metros: DiametroMetrosDto?
)

data class DiametroMetrosDto(
    @SerializedName("estimated_diameter_min") val minimo: Double?,
    @SerializedName("estimated_diameter_max") val maximo: Double?
)

data class AproximacionDto(
    @SerializedName("close_approach_date") val fecha: String?,
    @SerializedName("relative_velocity") val velocidad: VelocidadRelativaDto?,
    @SerializedName("miss_distance") val distancia: DistanciaPasoDto?
)

/** La API envía la velocidad como texto, por ejemplo "45678.1234567". */
data class VelocidadRelativaDto(
    @SerializedName("kilometers_per_hour") val kmPorHora: String?
)

/** La API envía la distancia como texto, por ejemplo "38457812.123". */
data class DistanciaPasoDto(
    @SerializedName("kilometers") val kilometros: String?
)
