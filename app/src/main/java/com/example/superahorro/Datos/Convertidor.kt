package com.example.superahorro.Datos

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Clase encargada de la transformación de archivos parseables a objetos, para su difusión por la aplicación
 */
class Convertidor {
    private val gson = Gson()

    // Para listas de amigos
    @TypeConverter
    fun fromListString(value: List<String>): String = gson.toJson(value)

    @TypeConverter
    fun toListString(value: String): List<String> =
        gson.fromJson(value, object : TypeToken<List<String>>() {}.type)

    // Para listas de tablas
    @TypeConverter
    fun fromListInt(value: List<Int>): String = gson.toJson(value)

    @TypeConverter
    fun toListInt(value: String): List<Int> =
        gson.fromJson(value, object : TypeToken<List<Int>>() {}.type)
}