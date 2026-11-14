package com.example.pc2_grupal.asteroides.data

/**
 * Convierte todo el feed en una sola lista de entidades.
 * Aplana el mapa fecha -> asteroides, ignora listas y elementos nulos y descarta los que no tienen id.
 * [idsFavoritos] sirve para conservar los favoritos al reemplazar los datos en Room.
 */
fun FeedDto.aEntities(idsFavoritos: Set<String>): List<AsteroideEntity> =
    asteroidesPorFecha.orEmpty().flatMap { (fechaDelFeed, asteroides) ->
        asteroides.orEmpty().filterNotNull().mapNotNull { dto ->
            dto.aEntity(fechaDelFeed = fechaDelFeed, idsFavoritos = idsFavoritos)
        }
    }

/**
 * Conversión explícita de un asteroide del feed (DTO) a la Entity de Room.
 * Devuelve null si falta el id, porque es la clave de la tabla.
 * Si no viene close_approach_date se usa la fecha del feed (la clave del mapa).
 */
fun NeoDto.aEntity(fechaDelFeed: String, idsFavoritos: Set<String>): AsteroideEntity? {
    val idValido = id?.takeIf { it.isNotBlank() } ?: return null
    val aproximacion = aproximaciones?.firstOrNull()
    return AsteroideEntity(
        id = idValido,
        nombre = nombre?.takeIf { it.isNotBlank() } ?: "Sin nombre",
        fechaAproximacion = aproximacion?.fecha?.takeIf { it.isNotBlank() } ?: fechaDelFeed,
        diametroMinM = diametroEstimado?.metros?.minimo ?: 0.0,
        diametroMaxM = diametroEstimado?.metros?.maximo ?: 0.0,
        // Velocidad y distancia llegan como texto: si no se pueden convertir quedan en 0.0
        velocidadKmh = aproximacion?.velocidad?.kmPorHora?.toDoubleOrNull() ?: 0.0,
        distanciaKm = aproximacion?.distancia?.kilometros?.toDoubleOrNull() ?: 0.0,
        esPeligroso = esPeligroso ?: false,
        esFavorito = idValido in idsFavoritos
    )
}
