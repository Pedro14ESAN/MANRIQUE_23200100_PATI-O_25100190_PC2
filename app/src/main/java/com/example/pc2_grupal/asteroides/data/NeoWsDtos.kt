package com.example.pc2_grupal.asteroides.data

import com.google.gson.annotations.SerializedName

/**
 * Respuesta de neo/rest/v1/feed. Los asteroides vienen agrupados por fecha:
 * un objeto cuyas claves son fechas y cuyos valores son listas de asteroides.
 * Todo es opcional para que la app no se cierre si falta algún campo.
 */
data class FeedResponseDto(
    @SerializedName("element_count") val cantidad: Int?,
    @SerializedName("near_earth_objects") val asteroidesPorFecha: Map<String, List<AsteroideDto>>?
)

data class AsteroideDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val nombre: String?,
    @SerializedName("is_potentially_hazardous_asteroid") val esPeligroso: Boolean?,
    @SerializedName("estimated_diameter") val diametroEstimado: DiametroEstimadoDto?,
    @SerializedName("close_approach_data") val aproximaciones: List<AproximacionDto>?
)

data class DiametroEstimadoDto(
    @SerializedName("meters") val metros: RangoDiametroDto?
)

data class RangoDiametroDto(
    @SerializedName("estimated_diameter_min") val minimo: Double?,
    @SerializedName("estimated_diameter_max") val maximo: Double?
)

data class AproximacionDto(
    @SerializedName("close_approach_date") val fecha: String?,
    @SerializedName("epoch_date_close_approach") val epoch: Long?,
    @SerializedName("relative_velocity") val velocidad: VelocidadDto?,
    @SerializedName("miss_distance") val distancia: DistanciaDto?
)

/** La API envía estos números como texto, por ejemplo "45678.123456". */
data class VelocidadDto(
    @SerializedName("kilometers_per_hour") val kmPorHora: String?
)

data class DistanciaDto(
    @SerializedName("kilometers") val kilometros: String?
)
