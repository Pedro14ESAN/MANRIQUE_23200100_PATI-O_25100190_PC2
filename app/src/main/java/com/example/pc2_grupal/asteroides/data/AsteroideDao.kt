package com.example.pc2_grupal.asteroides.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroideDao {

    // Solo desde la fecha indicada, para que no se acumulen los de días anteriores.
    // Los más recientes primero y, en la misma fecha, por nombre.
    @Query(
        "SELECT * FROM asteroides WHERE fecha_aproximacion >= :desde " +
            "ORDER BY fecha_aproximacion DESC, nombre ASC"
    )
    fun observarLista(desde: String): Flow<List<AsteroideEntity>>

    @Query(
        "SELECT * FROM asteroides WHERE fecha_aproximacion >= :desde AND es_peligroso = 1 " +
            "ORDER BY fecha_aproximacion DESC, nombre ASC"
    )
    fun observarPeligrosos(desde: String): Flow<List<AsteroideEntity>>

    @Query("SELECT * FROM asteroides WHERE es_favorito = 1 ORDER BY fecha_aproximacion DESC, nombre ASC")
    fun observarFavoritos(): Flow<List<AsteroideEntity>>

    @Query("SELECT * FROM asteroides WHERE id = :id")
    fun observarPorId(id: String): Flow<AsteroideEntity?>

    @Query("SELECT id FROM asteroides WHERE es_favorito = 1")
    suspend fun idsFavoritos(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(asteroides: List<AsteroideEntity>)

    @Query("UPDATE asteroides SET es_favorito = :esFavorito WHERE id = :id")
    suspend fun actualizarFavorito(id: String, esFavorito: Boolean)
}
