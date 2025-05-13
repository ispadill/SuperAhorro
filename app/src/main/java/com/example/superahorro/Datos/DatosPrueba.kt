package com.example.superahorro.Datos

import android.content.Context
import android.util.Log
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

/**
 * Clase para generar y cargar datos de prueba en la base de datos
 */
class DatosPrueba {
    companion object {
        /**
         * Genera y carga todos los datos de prueba en la base de datos
         */
        suspend fun cargarDatosPrueba(context: Context) {
            val db = BaseDeDatos.getDatabase(context.applicationContext)

            withContext(Dispatchers.IO) {
                db.tablaDao().deleteAll()
                db.usuarioDao().deleteAll()
                db.loggeadoDao().deleteAll()
                db.plantillaDao().deleteAll()

                val plantillas = crearPlantillas()
                plantillas.forEach { plantilla ->
                    db.plantillaDao().insert(plantilla)
                }

                val usuariosAnonimos = crearUsuariosAnonimos(3)
                val usuariosLoggeados = crearUsuariosLoggeados(10)

                val tablas = crearTablas(50, usuariosLoggeados, plantillas)

                val usuariosActualizados = distribuirTablasEntreUsuarios(usuariosLoggeados, tablas)

                usuariosAnonimos.forEach { anonimo ->
                    db.usuarioDao().insert(anonimo)
                }

                usuariosActualizados.forEach { loggeado ->
                    db.usuarioDao().insert(loggeado)
                    db.loggeadoDao().insert(loggeado)
                }

                tablas.forEach { tabla ->
                    db.tablaDao().insert(tabla)
                }
            }
        }

        /**
         * Crea las plantillas de prueba
         */
        private fun crearPlantillas(): List<Plantilla> {
            val plantillaDefault = Plantilla(
                nombre = "Prueba",
                numColumnas = 3,
                nombresColumnas = listOf("Tienda", "Producto", "Precio")
            )

            val plantillaDetallada = Plantilla(
                nombre = "Comparativa Detallada",
                numColumnas = 5,
                nombresColumnas = listOf("Tienda", "Producto", "Marca", "Cantidad", "Precio")
            )

            return listOf(plantillaDefault, plantillaDetallada)
        }

        /**
         * Crea usuarios anónimos
         */
        private fun crearUsuariosAnonimos(cantidad: Int): List<Anonimos> {
            return (1..cantidad).map {
                val id = "anonimo_${it}"
                Anonimos(id = id)
            }
        }

        /**
         * Crea usuarios loggeados
         */
        private fun crearUsuariosLoggeados(cantidad: Int): List<Loggeado> {
            val nombres = listOf("Juan", "Maria", "Pedro", "Ana", "Luis", "Laura", "Carlos", "Sofia", "Enrique", "Elena")
            Log.d("DATA_DEBUG", "Creando $cantidad usuarios loggeados")
            return (0 until cantidad).map { index ->
                val nombre = nombres[index % nombres.size]
                val id = nombre

                Loggeado(
                    id = id,
                    nombre = id ,
                    correo = "$id@gmail.com",
                    contraseña = "pass_$id",
                    listaAmigos = listOf(),
                    tablasPropias = listOf(),
                    tablasPublicas = listOf(),
                    tablasFavoritas = listOf()
                )
            }
        }

        /**
         * Crea tablas de prueba
         */
        private fun crearTablas(cantidad: Int, usuarios: List<Loggeado>, plantillas: List<Plantilla>): List<Tabla> {
            val tiendas = listOf("Mercadona", "Carrefour", "Lidl", "Aldi", "Dia", "Eroski", "Alcampo", "BM")
            val productos = listOf("Leche", "Pan", "Huevos", "Arroz", "Pasta", "Aceite", "Azúcar", "Sal", "Café", "Té")
            val temas = listOf("Alimentación", "Electrónica", "Hogar", "Ropa", "Ocio")

            return (1..cantidad).map { index ->
                val autorIndex = Random.nextInt(usuarios.size)
                val plantillaIndex = Random.nextInt(plantillas.size)
                val tema = temas[Random.nextInt(temas.size)]
                val tienda = tiendas[Random.nextInt(tiendas.size)]
                val producto = productos[Random.nextInt(productos.size)]

                Tabla(
                    titulo = "$tema - $tienda - $producto #$index",
                    autor = usuarios[autorIndex].id,
                    valoracion = (Random.nextFloat() * 5),
                    numeroValoraciones = Random.nextInt(1, 100),
                    plantillaId = plantillaIndex + 1
                )
            }
        }

        /**
         * Distribuye las tablas entre los usuarios loggeados
         */
        private fun distribuirTablasEntreUsuarios(usuarios: List<Loggeado>, tablas: List<Tabla>): List<Loggeado> {
            val usuariosConAmigos = usuarios.map { usuario ->
                val amigos = usuarios.filter { it.id != usuario.id }
                    .shuffled()
                    .take(Random.nextInt(1, usuarios.size))
                    .map { it.id }

                usuario.copy(listaAmigos = amigos)
            }

            return usuariosConAmigos.mapIndexed { index, usuario ->
                val tablasPropias = tablas.filter { it.autor == usuario.id }
                    .map { it.id }

                val tablasFavoritas = tablas.filter { it.autor != usuario.id }
                    .shuffled()
                    .take(Random.nextInt(3, 10))
                    .map { it.id }

                usuario.copy(
                    tablasPropias = tablasPropias,
                    tablasPublicas = listOf(), // Preguntar George
                    tablasFavoritas = tablasFavoritas
                )
            }
        }
    }
}