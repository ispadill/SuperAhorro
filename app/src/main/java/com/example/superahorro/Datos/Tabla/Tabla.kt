package com.example.superahorro.Datos

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tablas")
data class Tabla(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val autor: String,
    val valoracion: Float,
    val numeroValoraciones: Int,
    val plantillaId: Int
)