package com.example.superahorro.ModeloDominio

import com.example.superahorro.Datos.Loggeado
import java.util.Date

object Sesion {
    var usuario: Loggeado? = null
        set(value) {
            println("Sesion.usuario cambiado a: ${value?.id}")
            field = value
        }
    var fechaLogin: Date = Date()
        set(value) {
            println("Sesion.fechaLogin cambiado a: $value")
            field = value
        }
}
