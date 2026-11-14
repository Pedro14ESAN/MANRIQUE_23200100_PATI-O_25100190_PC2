package com.example.pc2_grupal.apod.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ApodDao {

    @Query("SELECT * FROM apod ORDER BY fecha DESC")
    fun observarTodas(): Flow<List<ApodEntity>>

    @Query("SELECT * FROM apod WHERE es_favorito = 1 ORDER BY fecha DESC")
    fun observarFavoritas(): Flow<List<ApodEntity>>

    @Query("SELECT * FROM apod WHERE fecha = :fecha")
    fun observarPorFecha(fecha: String): Flow<ApodEntity?>

    @Query("SELECT fecha FROM apod WHERE es_favorito = 1")
    suspend fun fechasFavoritas(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(imagenes: List<ApodEntity>)

    @Query("UPDATE apod SET es_favorito = :esFavorito WHERE fecha = :fecha")
    suspend fun actualizarFavorito(fecha: String, esFavorito: Boolean)
}
