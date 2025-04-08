package com.example.superahorro.ModeloDominio

/*
El id podría pasarse o crearse en el init,
podría ser algo como el nombre que le dió el
usuario al crearlo seguido de unos cuantos
números aleatorios
*/
class Tabla(
    val id: String,
    val titulo: String,
    val autor: String,
    var valoracion: Float,
    var numeroValoraciones: Int,
    val plantilla: Plantilla
){
    fun añadirValoracion(valo: Float){
        var auxiliar: Float
        auxiliar = valoracion*numeroValoraciones + valo
        numeroValoraciones++
        valoracion= auxiliar/numeroValoraciones
    }
}
