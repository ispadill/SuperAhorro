package com.example.superahorro.Datos

import androidx.room.*
import com.example.superahorro.Datos.Tabla
import kotlinx.coroutines.flow.Flow

@Dao
interface TablaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tabla: Tabla)

    @Update
    suspend fun update(tabla: Tabla)

    @Delete
    suspend fun delete(tabla: Tabla)

    @Query("SELECT * FROM tablas")
    fun getAllTablas(): Flow<List<Tabla>>

    @Query("SELECT * FROM tablas WHERE id = :id")
    fun getTablaById(id: Int): Flow<Tabla?>

    @Query("SELECT * FROM tablas WHERE autor = :autor")
    suspend fun getTablasDeUsuario(autor: String): List<Tabla>

    @Query("SELECT COUNT(*) FROM tablas")
    suspend fun countTablas(): Int

    @Query("DELETE FROM tablas")
    suspend fun deleteAll()

    @Query("SELECT * FROM tablas WHERE id IN (:ids)")
    suspend fun getTablasByIds(ids: List<Int>): List<Tabla>


}