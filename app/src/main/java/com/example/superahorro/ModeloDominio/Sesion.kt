package com.example.superahorro.ModeloDominio

import com.example.superahorro.Datos.Loggeado
import java.util.Date

object Sesion {
    var usuario: Loggeado? = null
    var fechaLogin: Date = Date()
}
