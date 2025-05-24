package com.example.superahorro.Datos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
open class Usuario(
    @PrimaryKey open var id: String,
    var tipo: String
)