package com.example.pc2_grupal.asteroides.data

/** Aplana el feed (agrupado por fecha) en una sola lista de DTOs. */
fun FeedResponseDto.todosLosAsteroides(): List<AsteroideDto> =
    asteroidesPorFecha?.values?.flatMap { it.orEmpty() }.orEmpty()

/**
 * Conversión explícita DTO -> Entity.
 * Devuelve null si falta el id, porque es la clave de la tabla.
 */
fun AsteroideDto.aEntity(esFavorito: Boolean = false): AsteroideEntity? {
    val idValido = id?.takeIf { it.isNotBlank() } ?: return null
    val aproximacion = aproximaciones?.firstOrNull()
    return AsteroideEntity(
        id = idValido,
        nombre = nombre?.takeIf { it.isNotBlank() } ?: "Sin nombre",
        fechaAproximacion = aproximacion?.fecha ?: "Sin fecha",
        epochAproximacion = aproximacion?.epoch,
        diametroMinM = diametroEstimado?.metros?.minimo,
        diametroMaxM = diametroEstimado?.metros?.maximo,
        // Velocidad y distancia llegan como texto
        velocidadKmH = aproximacion?.velocidad?.kmPorHora?.toDoubleOrNull(),
        distanciaKm = aproximacion?.distancia?.kilometros?.toDoubleOrNull(),
        esPeligroso = esPeligroso == true,
        esFavorito = esFavorito
    )
}
