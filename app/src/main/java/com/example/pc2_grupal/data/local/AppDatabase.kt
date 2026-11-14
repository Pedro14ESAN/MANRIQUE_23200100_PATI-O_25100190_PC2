package com.example.pc2_grupal.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pc2_grupal.apod.data.ApodDao
import com.example.pc2_grupal.apod.data.ApodEntity
import com.example.pc2_grupal.asteroides.data.AsteroideDao
import com.example.pc2_grupal.asteroides.data.AsteroideEntity

/**
 * Base de datos SQLite compartida por las dos opciones.
 * Para agregar la tabla de asteroides: sumar la Entity en `entities`,
 * agregar su DAO abajo y subir `version` en 1.
 */
@Database(
    entities = [ApodEntity::class, AsteroideEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun apodDao(): ApodDao

    abstract fun asteroideDao(): AsteroideDao

    companion object {
        @Volatile
        private var instancia: AppDatabase? = null

        fun obtener(context: Context): AppDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "diario_espacial.db"
                )
                    // Los datos vienen de la API, así que si cambia el esquema se recrea la base
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also { instancia = it }
            }
    }
}
