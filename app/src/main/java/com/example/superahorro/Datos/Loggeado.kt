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
    var nombre: String,
    var correo: String,
    var contraseña: String,
    var listaAmigos: List<String>,
    var tablasPropias: List<Int>,
    var tablasPublicas: List<Int>,
    var tablasFavoritas: List<Int>,
    var imagenPerfilUri: String? = null
) : Usuario(id = id, tipo = "LOGEADO")