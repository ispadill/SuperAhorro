package com.example.superahorro.Datos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plantillas")
data class Plantilla(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val numColumnas: Int,
    val nombresColumnas: List<String>
)