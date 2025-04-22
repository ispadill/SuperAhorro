package com.example.superahorro.ModeloDominio

/*
Los métodos de set y get vienen por defecto,
así que se debería poder cambiar las listas o
sacarlas y añadir o quitar un elemento
 */
class Loggeado(
    var id: String,
    var contraseña: String,
    var listaAmigos: List<String>,
    var tablasPropias: List<Tabla>,
    var tablasPublicas: List<Tabla>,
    var tablasFavoritas: List<Tabla>): Usuario(id){

}