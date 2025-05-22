package com.example.superahorro.Datos

import com.example.superahorro.Datos.Tabla
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Repositorio que proporciona acceso a la tabla de tablas
 */
class TablaRepository(private val tablaDao: TablaDAO) {
    /**
     * Obtiene todas las tablas de la base de datos
     */
    fun getAllTablas(): Flow<List<Tabla>> = tablaDao.getAllTablas()

    /**
     * Inserta una tabla en la base de datos
     */
    suspend fun insertTabla(tabla: Tabla) = tablaDao.insert(tabla)

    /**
     * Elimina una tabla de la base de datos
     */
    suspend fun deleteTabla(tabla: Tabla) = tablaDao.delete(tabla)

    /**
     * Actualiza una tabla en la base de datos
     */
    suspend fun updateTabla(tabla: Tabla) = tablaDao.update(tabla)

    /**
     * Obtiene una tabla por su ID
     */
    suspend fun getTablaById(tablaId: Int): Tabla? {
        return withContext(Dispatchers.IO) {
            tablaDao.getTablaById(tablaId)
        }
    }

    /**
     * Obtiene todas las tablas de un usuario
     */
    suspend fun getTablasDeUsuario(autor: String): List<Tabla> = tablaDao.getTablasDeUsuario(autor)

    /**
     * Obtiene el número total de tablas
     */
    suspend fun countTablas(): Int = tablaDao.countTablas()

    suspend fun deleteAll()=tablaDao.deleteAll()

    suspend fun insertAndGetId(tabla: Tabla): Long=tablaDao.insertAndGetId(tabla)
}
