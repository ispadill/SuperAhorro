package com.example.superahorro.ModeloDominio

/*
Las plantillas las creamos los desarrolladores,
Al crear la plantilla el numColumnas lo sacamos
de la lista con .size
 */
class Plantilla(
    val nombre: String,
    val numColumnas: Int,
    val nombresColumnas: List<String>
) {

}