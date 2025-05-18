package com.example.superahorro.Datos

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.room.Room
import com.example.superahorro.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
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
            val directorioImagenes = context.getDir("profile_images", Context.MODE_PRIVATE)

            withContext(Dispatchers.IO) {
                db.tablaDao().deleteAll()
                db.usuarioDao().deleteAll()
                db.loggeadoDao().deleteAll()
                db.plantillaDao().deleteAll()
                db.anonimosDao().deleteAll()

                val defaultBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.logoapp)
                val defaultFile = File(directorioImagenes, "default.jpg")
                FileOutputStream(defaultFile).use { output ->
                    defaultBitmap.compress(Bitmap.CompressFormat.PNG, 90, output)
                }

                val plantillas = crearPlantillas()
                plantillas.forEach { plantilla ->
                    db.plantillaDao().insert(plantilla)
                }

                val usuariosAnonimos = crearUsuariosAnonimos(3)
                val usuariosLoggeados = crearUsuariosLoggeados(10)

                usuariosAnonimos.forEach { anonimo ->
                    db.usuarioDao().insert(anonimo)
                    db.anonimosDao().insert(anonimo)
                }

                usuariosLoggeados.forEach { loggeado ->
                    val loggeadoInicial = loggeado.copy(
                        tablasPropias = emptyList(),
                        tablasPublicas = emptyList(),
                        tablasFavoritas = emptyList(),
                        imagenPerfilUri = defaultFile.absolutePath
                    )
                    db.usuarioDao().insert(loggeadoInicial)
                    db.loggeadoDao().insert(loggeadoInicial)
                }

                val tablasCreadas = crearTablas(50, usuariosLoggeados, plantillas)
                val tablasConIds = mutableListOf<Tabla>()

                tablasCreadas.forEach { tabla ->
                    val id = db.tablaDao().insertAndGetId(tabla)
                    tablasConIds.add(tabla.copy(id = id.toInt()))
                }

                val usuariosActualizados = distribuirTablasEntreUsuarios(usuariosLoggeados, tablasConIds)

                usuariosActualizados.forEach { loggeado ->
                    db.loggeadoDao().update(loggeado)
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

            val usuariosBasicos = (0 until cantidad).map { index ->
                val nombre = nombres[index % nombres.size]
                val id = nombre

                Loggeado(
                    id = id,
                    nombre = id,
                    correo = "$id@gmail.com",
                    contraseña = "pass_$id",
                    listaAmigos = listOf(),
                    tablasPropias = listOf(),
                    tablasPublicas = listOf(),
                    tablasFavoritas = listOf(),
                    imagenPerfilUri = "logoapp"
                )
            }

            return usuariosBasicos.map { usuario ->
                val amigos = usuariosBasicos
                    .filter { it.id != usuario.id }
                    .shuffled()
                    .take(Random.nextInt(1, usuariosBasicos.size))
                    .map { it.id }

                usuario.copy(listaAmigos = amigos)
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
            return usuarios.map { usuario ->
                val tablasPropias = tablas
                    .filter { it.autor == usuario.id }
                    .map { it.id }

                val tablasFavoritas = tablas
                    .filter { it.autor != usuario.id }
                    .shuffled()
                    .take(Random.nextInt(3, 10).coerceAtMost(tablas.size / 2))
                    .map { it.id }

                Log.d("DATA_DEBUG", "Usuario ${usuario.id} - Tablas propias: ${tablasPropias.size}, Favoritas: ${tablasFavoritas.size}")

                Log.d("DATA_DEBUG", "IDs Tablas propias: $tablasPropias")
                Log.d("DATA_DEBUG", "IDs Tablas favoritas: $tablasFavoritas")

                usuario.copy(
                    tablasPropias = tablasPropias,
                    tablasPublicas = tablasPropias.shuffled().take(tablasPropias.size / 2),
                    tablasFavoritas = tablasFavoritas
                )
            }
        }
    }
}