package com.example.superahorro.Datos

import androidx.room.*
import com.example.superahorro.Datos.Plantilla
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantillaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plantilla: com.example.superahorro.Datos.Plantilla)

    @Query("SELECT * FROM plantillas")
    fun getAllPlantillas(): Flow<List<Plantilla>>

    @Query("DELETE FROM plantillas")
    suspend fun deleteAll()
}