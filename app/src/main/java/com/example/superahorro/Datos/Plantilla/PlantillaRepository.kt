package com.example.superahorro.Datos

import com.example.superahorro.Datos.Plantilla
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio que proporciona acceso a la tabla de plantillas
 */
class PlantillaRepository(private val plantillaDao: PlantillaDAO) {
    /**
     * Obtiene todas las plantillas de la base de datos
     */
    fun getAllPlantillas(): Flow<List<Plantilla>> = plantillaDao.getAllPlantillas()

    /**
     * Inserta una plantilla en la base de datos
     */
    suspend fun insertPlantilla(plantilla: Plantilla) = plantillaDao.insert(plantilla)

    suspend fun deleteAll()=plantillaDao.deleteAll()
}
