package com.example.pc2_grupal.asteroides.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroideDao {

    // Los más recientes primero
    @Query("SELECT * FROM asteroides ORDER BY fecha_aproximacion DESC, epoch_aproximacion DESC")
    fun observarTodos(): Flow<List<AsteroideEntity>>

    @Query("SELECT * FROM asteroides WHERE es_favorito = 1 ORDER BY fecha_aproximacion DESC, epoch_aproximacion DESC")
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
