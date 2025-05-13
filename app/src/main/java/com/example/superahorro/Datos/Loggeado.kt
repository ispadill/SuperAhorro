package com.example.superahorro.Datos

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "loggeados",
    indices = [Index("id")],
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Loggeado(
    @PrimaryKey override var id: String,
    val contraseña: String,
    val listaAmigos: List<String>,
    val tablasPropias: List<Int>,
    val tablasPublicas: List<Int>,
    val tablasFavoritas: List<Int>
) : Usuario(id = id, tipo = "LOGEADO")