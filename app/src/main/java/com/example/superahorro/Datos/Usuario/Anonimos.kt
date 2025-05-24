package com.example.superahorro.Datos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anonimos")
data class Anonimos(
    @PrimaryKey override var id: String,
) : Usuario(id = id, tipo = "ANONIMO")