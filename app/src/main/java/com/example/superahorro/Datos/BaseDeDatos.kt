package com.example.superahorro.Datos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        Usuario::class,
        Loggeado::class,
        Anonimos::class,
        Tabla::class,
        Plantilla::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Convertidor::class)
abstract class BaseDeDatos : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDAO
    abstract fun loggeadoDao(): LoggeadoDAO
    abstract fun tablaDao(): TablaDAO
    abstract fun plantillaDao(): PlantillaDAO

    companion object {
        @Volatile
        private var Instance: BaseDeDatos? = null

        fun getDatabase(context: Context): BaseDeDatos {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, BaseDeDatos::class.java, "superahorro_basedatos")
                    .fallbackToDestructiveMigration()
                    .build().also { Instance = it }
            }
        }
    }
}